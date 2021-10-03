package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.LocaleImageType
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.createbusiness.LocaleImage
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep3Binding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.DocumentsRecyclerAdapter
import com.technzone.miniborsa.ui.business.createbusiness.adapters.ImageRecyclerAdapter
import com.technzone.miniborsa.utils.ContentUriUtils.getFilePathFromURI
import com.technzone.miniborsa.utils.ImagePickerUtil.Companion.TAKE_USER_IMAGE_REQUEST_CODE
import com.technzone.miniborsa.utils.extensions.calculatePercentage
import com.technzone.miniborsa.utils.pickImages
import com.technzone.miniborsa.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.row_sort.*

@AndroidEntryPoint
class CreateBusinessStep3Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep3Binding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter

    lateinit var documentsAdapter: DocumentsRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step3

    override fun onResume() {
        super.onResume()
        loadBusinessData()
    }
    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvImages()
        setUpRvDocuments()
    }

    private fun setUpListeners() {

    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpRvImages() {
        imageRecyclerAdapter = ImageRecyclerAdapter(requireContext())
        binding?.rvPhotos?.adapter = imageRecyclerAdapter
        binding?.rvPhotos?.setOnItemClickListener(this)
        binding?.rvPhotos?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        val photosLayoutManager = GridLayoutManager(requireContext(), 3)

        photosLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (imageRecyclerAdapter.itemCount - 1 == 0) {
                    3
                } else if (imageRecyclerAdapter.itemCount - 1 > 0 && position == 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding?.rvPhotos?.layoutManager = photosLayoutManager
//        refreshImages()
    }

    private fun setUpRvDocuments() {
        documentsAdapter = DocumentsRecyclerAdapter(requireContext())
        binding?.rvDocuments?.adapter = documentsAdapter
        binding?.rvDocuments?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as LocaleImage
                if (view?.id == R.id.imgRemove) {
                    item.id?.let {
                        viewModel.deleteFile(it)
                            .observe(this@CreateBusinessStep3Fragment, uploadResultObserver())
                    }
                } else {
                    openDocumentPicker()
                }
            }
        })
//        refreshDocuments()
    }

    private fun openDocumentPicker() {
        val intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        val mimetypes = arrayOf("application/pdf")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra("CONTENT_TYPE", "application/pdf");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(
            Intent.createChooser(intent, "Choose Pdf"),
            REQUEST_CODE_PICK_FILE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return
        when (requestCode) {
            TAKE_USER_IMAGE_REQUEST_CODE -> {
                val fileUri = data.data
                if (imageRecyclerAdapter.itemCount == 1)
                    fileUri?.path?.let {
                        viewModel.addIconImage(it).observe(this, uploadResultObserver())
                    }
                else fileUri?.path?.let {
                    viewModel.addImage(it).observe(this, uploadResultObserver())
                }
            }
            REQUEST_CODE_MAIN_IMAGE -> {
                val fileUri = data.data
                fileUri?.path?.let {
                    viewModel.addIconImage(it).observe(this, uploadResultObserver())
                }
            }
            REQUEST_CODE_PICK_FILE -> {
                var realPath = data.data?.getFilePathFromURI(requireContext())
                if (realPath == null)
                    realPath = data.data?.toString()
                realPath?.let { viewModel.addFile(it).observe(this, uploadResultObserver()) }
            }
        }
    }

    private fun uploadResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                   loadBusinessData()
                }
            })
    }
    private fun loadBusinessData(){
        if (!viewModel.hasBusiness) {
            viewModel.getCompanyRequest()
                .observe(this@CreateBusinessStep3Fragment, businessResultObserver())
        } else {
            viewModel.getBusinessRequest()
                .observe(this@CreateBusinessStep3Fragment, businessResultObserver())
        }
    }
    private fun businessResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    data?.images?.toMutableList()?.let {
                        it.apply {
                            data.icon?.let {
                                add(0, Media(name = it, id = -1))
                            }
                        }
                        viewModel.images = it
                        imageRecyclerAdapter.submitNewItems(it.map {
                            LocaleImage(
                                path = it.name,
                                id = it.id
                            )
                        })
                    } ?: also {
                        viewModel.images = mutableListOf()
                        viewModel.images.apply {
                            data?.icon?.let {
                                add(0, Media(name = it, id = -1))
                            }
                        }
                    }
                    data?.files?.toMutableList()?.let {
                        viewModel.files = it
                        documentsAdapter.submitNewItems(it.map {
                            LocaleImage(
                                path = it.name,
                                id = it.id
                            )
                        })
                    } ?: also { viewModel.files = mutableListOf() }
                    refreshPhotosAndDocs()
                }
            })
    }

    private fun addImage() {
        imageRecyclerAdapter.submitItem(
            LocaleImage(
                path = null,
                contentType = LocaleImageType.ADD_IMAGE
            )
        )
    }

    private fun addFirstImage() {
        imageRecyclerAdapter.submitItem(
            LocaleImage(
                path = null,
                contentType = LocaleImageType.ADD_FIRST
            )
        )
    }

    private fun refreshPhotosAndDocs() {
        refreshImages()
        refreshDocuments()
    }

    private fun refreshImages() {
        imageRecyclerAdapter.submitNewItems(viewModel.images.map {
            LocaleImage(
                path = it.name,
                id = it.id,
                contentType = LocaleImageType.IMAGE
            )
        })
        when (imageRecyclerAdapter.itemCount) {
            0 -> {
                addFirstImage()
            }
            1 -> {
                if (imageRecyclerAdapter.items[0].contentType == LocaleImageType.ADD_IMAGE) {
                    imageRecyclerAdapter.items.removeAt(imageRecyclerAdapter.itemCount - 1)
                    addFirstImage()
                } else if (imageRecyclerAdapter.items[0].contentType != LocaleImageType.ADD_FIRST) {
                    addImage()
                }
            }
            4 -> {
                if (imageRecyclerAdapter.items[3].contentType != LocaleImageType.ADD_IMAGE) {
                    addImage()
                }
            }
            2, 3 -> {
                addImage()
            }
        }
        calculatePercentage()
    }

    private fun refreshDocuments() {
        documentsAdapter.submitNewItems(viewModel.files.map {
            LocaleImage(
                path = it.name,
                id = it.id,
                contentType = LocaleImageType.IMAGE
            )
        })
        when (documentsAdapter.itemCount) {
            0 -> {
                addFirstDocument()
            }
            1 -> {
                if (documentsAdapter.items[0].contentType == LocaleImageType.ADD_IMAGE) {
                    documentsAdapter.items.removeAt(documentsAdapter.itemCount - 1)
                    addFirstDocument()
                } else if (documentsAdapter.items[0].contentType != LocaleImageType.ADD_FIRST) {
                    addDocument()
                }
            }
            4 -> {
                if (documentsAdapter.items[3].contentType != LocaleImageType.ADD_IMAGE) {
                    addDocument()
                }
            }
            2, 3 -> {
                addDocument()
            }
        }
        calculatePercentage()
    }

    private fun addDocument() {
        documentsAdapter.submitItem(
            LocaleImage(
                path = null,
                contentType = LocaleImageType.ADD_IMAGE
            )
        )
    }

    private fun addFirstDocument() {
        documentsAdapter.submitItem(
            LocaleImage(
                path = null,
                contentType = LocaleImageType.ADD_FIRST
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as LocaleImage
        if (view?.id == R.id.imgRemove) {
            if (position == 0) {
                pickImages(
                    requestCode = REQUEST_CODE_MAIN_IMAGE
                )
//                viewModel.deleteCompanyIcon().observe(this, uploadResultObserver())
            } else {
                item.id?.let { viewModel.deleteImage(it).observe(this, uploadResultObserver()) }
            }
        } else {
            pickImages(
                requestCode = TAKE_USER_IMAGE_REQUEST_CODE
            )
        }
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        viewModel.updateRequestBusiness().observe(this, updateRequestResultObserver(callback))
    }

    private fun updateRequestResultObserver(
        callback: (Boolean) -> Unit
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    callback(true)
                }
            })
    }

    override fun calculatePercentage() {
        viewModel.percentage.postValue(viewModel.buildBusinessRequest().calculatePercentage())
    }


    companion object {
        const val REQUEST_CODE_PICK_FILE = 232
        const val REQUEST_CODE_MAIN_IMAGE = 234
    }
}