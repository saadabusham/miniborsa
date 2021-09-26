package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.LocaleImageType
import com.technzone.miniborsa.data.models.createbusiness.LocaleImage
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep3Binding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.DocumentsRecyclerAdapter
import com.technzone.miniborsa.ui.business.createbusiness.adapters.ImageRecyclerAdapter
import com.technzone.miniborsa.utils.ContentUriUtils.getFilePathFromURI
import com.technzone.miniborsa.utils.ImagePickerUtil.Companion.TAKE_USER_IMAGE_REQUEST_CODE
import com.technzone.miniborsa.utils.pickImages
import com.technzone.miniborsa.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep3Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep3Binding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter

    lateinit var documentsAdapter: DocumentsRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step3

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
        refreshImages()
    }

    private fun setUpRvDocuments() {
        documentsAdapter = DocumentsRecyclerAdapter(requireContext())
        binding?.rvDocuments?.adapter = documentsAdapter
        binding?.rvDocuments?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as LocaleImage
                if (view?.id == R.id.imgRemove) {
                    documentsAdapter.items.removeAt(position)
                    documentsAdapter.notifyItemRemoved(position)
                    refreshDocuments()
                } else {
                    openDocumentPicker()
                }
            }
        })
        refreshDocuments()
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
//                imageRecyclerAdapter.submitItemToPosition(
//                    LocaleImage(
//                        path = fileUri?.path,
//                        contentType = LocaleImageType.IMAGE
//                    ), imageRecyclerAdapter.itemCount - 1
//                )
                fileUri?.path?.let { viewModel.addImage(it).observe(this,uploadResultObserver()) }
            }
            REQUEST_CODE_PICK_FILE -> {
                var realPath = data.data?.getFilePathFromURI(requireContext())
                if (realPath == null)
                    realPath = data.data?.toString()
//                documentsAdapter.submitItemToPosition(
//                    LocaleImage(
//                        path = realPath,
//                        contentType = LocaleImageType.IMAGE
//                    ), documentsAdapter.itemCount - 1
//                )

                realPath?.let { viewModel.addImage(it).observe(this,uploadResultObserver()) }
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
    private fun refreshPhotosAndDocs(){
        refreshImages()
        refreshDocuments()
    }
    private fun refreshImages() {
        imageRecyclerAdapter.submitNewItems(viewModel.images.map { LocaleImage(path = it.name,id = it.id) })
        when (imageRecyclerAdapter.itemCount) {
            0 -> {
                addFirstImage()
            }
            1 -> {
                if (imageRecyclerAdapter.items[0].contentType == LocaleImageType.ADD_IMAGE) {
                    imageRecyclerAdapter.items.removeAt(imageRecyclerAdapter.itemCount - 1)
                    addFirstImage()
                }
            }
            5 -> {
                if (imageRecyclerAdapter.items[4].contentType != LocaleImageType.ADD_IMAGE) {
                    addImage()
                }
            }
            6 -> {
                imageRecyclerAdapter.items.removeAt(5)
                imageRecyclerAdapter.notifyItemRemoved(5)
            }
            else -> {
                imageRecyclerAdapter.items.removeAt(imageRecyclerAdapter.itemCount - 1)
                addImage()
            }
        }
    }

    private fun refreshDocuments() {
        documentsAdapter.submitNewItems(viewModel.images.map { LocaleImage(path = it.name,id = it.id) })
        when (documentsAdapter.itemCount) {
            0 -> {
                addFirstDocument()
            }
            1 -> {
                if (documentsAdapter.items[0].contentType == LocaleImageType.ADD_IMAGE) {
                    documentsAdapter.items.removeAt(documentsAdapter.itemCount - 1)
                    addFirstDocument()
                }
            }
            5 -> {
                if (documentsAdapter.items[4].contentType != LocaleImageType.ADD_IMAGE) {
                    addFirstDocument()
                }
            }
            6 -> {
                documentsAdapter.items.removeAt(5)
                documentsAdapter.notifyItemRemoved(5)
            }
            else -> {
                documentsAdapter.items.removeAt(documentsAdapter.itemCount - 1)
                addDocument()
            }
        }
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
            imageRecyclerAdapter.items.removeAt(position)
            imageRecyclerAdapter.notifyItemRemoved(position)
            refreshImages()
        } else {
            pickImages(
                requestCode = TAKE_USER_IMAGE_REQUEST_CODE
            )
        }
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

    companion object {
        const val REQUEST_CODE_PICK_FILE = 232
    }
}