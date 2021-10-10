package com.technzone.minibursa.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.models.map.Address
import com.technzone.minibursa.databinding.ActivityMapBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.utils.displayLocationSettingsRequest
import com.technzone.minibursa.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.disposables.Disposable
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
@AndroidEntryPoint
class MapActivity : BaseBindingActivity<ActivityMapBinding>(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    private var disposable: Disposable? = null

    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_map,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false
        )
        setUp()
    }
    private fun setUp(){
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        initAutoCompletePlaces()
        initWithPermissionCheck()
        setUpListeners()
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun init() {
        enableLocationServices()
    }

    private fun enableLocationServices() {
        if (!SmartLocation.with(this).location().state()
                .locationServicesEnabled()
        ) {
            displayLocationSettingsRequest(
                REQUEST_CODE_LOCATION
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        requestLocationPermission(object : MyMultiPermissionListeners {
            override fun onPermissionGranted() {
                binding?.progressBar?.relativeProgress?.visible()
                SmartLocation.with(this@MapActivity)
                    .location()
                    .oneFix()
                    .start { location ->
                        zoomToLocation(location.latitude, location.longitude)
                    }
            }

            override fun onPermissionDenied() {
            }

        }, true)

    }

    private fun zoomToLocation(latitude: Double, longitude: Double) {
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                ZOOM_LEVEL
            )
        )
        binding?.progressBar?.relativeProgress?.gone()
    }

    private fun setUpListeners() {

        binding?.btnNext?.setOnClickListener {
            onNextClicked()
        }
        binding?.fabGetCurrentLocation?.setOnClickListener {
            onGetMyLocationClicked()
        }
    }

    fun onNextClicked() {
        val location = getLatLng()
        if (location == null) {
            showErrorAlert(
                resources.getString(R.string.location),
                resources.getString(R.string.please_pick_location)
            )
            return
        }
        val address = location.let { Address(it.latitude, it.longitude) }
        setResult(RESULT_OK,Intent().apply {
            this.putExtra(Constants.BundleData.ADDRESS,address)
        })
        finish()
    }

    private fun getLatLng(): LatLng? {
        val array = IntArray(2)
        mapFragment.view?.getLocationOnScreen(array)
        val mapX = array[0]
        val mapY = array[1]

        val mapWidth = mapFragment.view?.width ?: 0
        val mapHeight = mapFragment.view?.height ?: 0
        val mapCenterX = (mapX + mapWidth / 2)
        val mapCenterY = (mapY + mapHeight / 2)
        return googleMap?.projection?.fromScreenLocation(Point(mapCenterX, mapCenterY))
    }

    fun onGetMyLocationClicked() {
        initWithPermissionCheck()
    }

    private fun initAutoCompletePlaces() {
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        )
        binding?.cvSearch?.setOnClickListener {
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).setCountries(listOf("JO"))
                .build(this)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.uiSettings?.isCompassEnabled = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                val states = LocationSettingsStates.fromIntent(data)
                when (resultCode) {
                    RESULT_OK -> {
                        initWithPermissionCheck()
                    }
                }
            }
            REQUEST_CODE_AUTOCOMPLETE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        val address = place.address ?: ""
                        googleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                place?.latLng,
                                14f
                            )
                        )
                        place.latLng?.latitude?.let {
                            place.latLng?.longitude?.let { it1 ->
                                zoomToLocation(
                                    it,
                                    it1
                                )
                            }
                        }
                        binding?.tvSearch?.text = address
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    companion object {

        const val REQUEST_CODE_LOCATION = 99
        const val ZOOM_LEVEL = 16f
        const val REQUEST_CODE_AUTOCOMPLETE = 2025
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, MapActivity::class.java)
            context?.startActivity(intent)
        }

        fun start(
            context: Activity?,
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, MapActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

}