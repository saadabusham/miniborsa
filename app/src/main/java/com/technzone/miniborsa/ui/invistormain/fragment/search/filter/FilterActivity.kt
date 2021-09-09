package com.technzone.miniborsa.ui.invistormain.fragment.search.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.ActivityChooseGeneralBinding
import com.technzone.miniborsa.databinding.ActivityFilterBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.general.adapters.ChooseGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.invistormain.fragment.search.filter.adapters.GeneralLookupRecyclerAdapter
import com.technzone.miniborsa.ui.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FilterActivity : BaseBindingActivity<ActivityFilterBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by viewModels()
    private lateinit var businessTypeAdapter: GeneralLookupRecyclerAdapter
    private lateinit var inustryAdapter: GeneralLookupRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter, hasToolbar = false)
        setUpBinding()
        setUpListeners()
        setUpRvIndustries()
        setUpRvBusinessType()
    }


    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            onBackPressed()
        }

        binding?.rangeSeekBar?.setOnRangeSeekbarChangeListener { minValue: Number, maxValue: Number ->
//            filter.minPrice = minValue.toFloat()
//            filter.maxPrice = maxValue.toFloat()
//            tvMin.setText(minValue.toString() + " " + getString(R.string.jd));
//            tvMax.setText(maxValue.toString() + " " + getString(R.string.jd));
            binding?.tvMin?.text = String.format(getString(R.string.price_), minValue.toString())
            binding?.tvMax?.text = String.format(getString(R.string.price_), maxValue.toString())
        }
        binding?.rangeSeekBar?.setMinValue(100000f)?.apply()
        binding?.rangeSeekBar?.setMaxValue(100000000f)?.apply()
    }

    private fun setUpRvIndustries() {
        inustryAdapter = GeneralLookupRecyclerAdapter(this)
        binding?.rvIndustry?.adapter = inustryAdapter
        binding?.rvIndustry?.setOnItemClickListener(this)
        binding?.rvIndustry?.itemAnimator = null
        inustryAdapter.submitItems(
            arrayListOf(
                GeneralLookup(name = "Blockchain"),
                GeneralLookup(name = "Agriculture"),
                GeneralLookup(name = "Robotics"),
                GeneralLookup(name = "EdTech"),
                GeneralLookup(name = "Food & Beverage"),
                GeneralLookup(name = "IT"),
            )
        )
    }

    private fun setUpRvBusinessType() {
        businessTypeAdapter = GeneralLookupRecyclerAdapter(this)
        binding?.rvBusinessType?.adapter = businessTypeAdapter
        binding?.rvBusinessType?.setOnItemClickListener(this)
        binding?.rvBusinessType?.itemAnimator = null
        businessTypeAdapter.submitItems(
            arrayListOf(
                GeneralLookup(name = "Business for Sale"),
                GeneralLookup(name = "Share For Sale"),
                GeneralLookup(name = "Franchise")
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }
    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, FilterActivity::class.java)
            context?.startActivity(intent)
        }

    }

}