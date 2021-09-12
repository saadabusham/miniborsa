package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.LocaleImageType
import com.technzone.miniborsa.data.models.createbusiness.LocaleImage
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep3Binding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.MediaRecyclerAdapter
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import com.technzone.miniborsa.utils.ImagePickerUtil.Companion.TAKE_USER_IMAGE_REQUEST_CODE
import com.technzone.miniborsa.utils.pickImages
import com.technzone.miniborsa.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep3Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep3Binding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: CreateBusinessViewModel by activityViewModels()

    private lateinit var mediaAdapter: MediaRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step3

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvImages()
    }

    private fun setUpListeners() {

    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpRvImages() {
        mediaAdapter = MediaRecyclerAdapter(requireContext())
        binding?.layoutMedia?.rvPhotos?.adapter = mediaAdapter
        binding?.layoutMedia?.rvPhotos?.setOnItemClickListener(this)
        binding?.layoutMedia?.rvPhotos?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        val photosLayoutManager = GridLayoutManager(requireContext(), 3)

        photosLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mediaAdapter.itemCount - 1 == 0) {
                    3
                } else if (mediaAdapter.itemCount - 1 > 0 && position == 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding?.layoutMedia?.rvPhotos?.layoutManager = photosLayoutManager
        addFirstImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            TAKE_USER_IMAGE_REQUEST_CODE -> {
                val fileUri = data?.data
                mediaAdapter.submitItemToPosition(
                    LocaleImage(
                        path = fileUri?.path,
                        contentType = LocaleImageType.IMAGE
                    ), mediaAdapter.itemCount - 1
                )
                refreshImages()
            }
        }
    }

    private fun addImage() {
        mediaAdapter.submitItem(LocaleImage(path = null, contentType = LocaleImageType.ADD_IMAGE))
    }

    private fun addFirstImage() {
        mediaAdapter.submitItem(LocaleImage(path = null, contentType = LocaleImageType.ADD_FIRST))
    }

    private fun refreshImages() {
        when (mediaAdapter.itemCount) {
            0 -> {
                addFirstImage()
            }
            1 -> {
                if (mediaAdapter.items[0].contentType == LocaleImageType.ADD_IMAGE) {
                    mediaAdapter.items.removeAt(mediaAdapter.itemCount - 1)
                    addFirstImage()
                }
            }
            5 -> {
                if (mediaAdapter.items[4].contentType != LocaleImageType.ADD_IMAGE) {
                    addImage()
                }
            }
            6 -> {
                mediaAdapter.items.removeAt(5)
                mediaAdapter.notifyItemRemoved(5)
            }
            else -> {
                mediaAdapter.items.removeAt(mediaAdapter.itemCount - 1)
                addImage()
            }
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as LocaleImage
        if (view?.id == R.id.imgRemove) {
            mediaAdapter.items.removeAt(position)
            mediaAdapter.notifyItemRemoved(position)
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

}