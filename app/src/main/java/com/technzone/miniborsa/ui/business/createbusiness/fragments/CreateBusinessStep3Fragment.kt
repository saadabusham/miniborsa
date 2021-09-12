package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
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
        binding?.layoutMedia?.recyclerView?.adapter = mediaAdapter
        binding?.layoutMedia?.recyclerView?.setOnItemClickListener(this)
        binding?.layoutMedia?.recyclerView?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
        addImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            TAKE_USER_IMAGE_REQUEST_CODE -> {
                val fileUri = data?.data
                mediaAdapter.submitItemToTop(
                    LocaleImage(
                        path = fileUri?.path,
                        contentType = if (mediaAdapter.itemCount == 1) LocaleImageType.MAIN_IMAGE else LocaleImageType.IMAGE
                    )
                )
                if (mediaAdapter.itemCount == 6) {
                    mediaAdapter.items.removeAt(5)
                    mediaAdapter.notifyItemRemoved(5)
                }
            }
        }
    }

    private fun addImage() {
        mediaAdapter.submitItem(LocaleImage(path = null, contentType = LocaleImageType.ADD_IMAGE))
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as LocaleImage
        if (view?.id == R.id.imgRemove) {
            mediaAdapter.items.removeAt(position)
            mediaAdapter.notifyItemRemoved(position)
            if (mediaAdapter.itemCount == 4)
                addImage()
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