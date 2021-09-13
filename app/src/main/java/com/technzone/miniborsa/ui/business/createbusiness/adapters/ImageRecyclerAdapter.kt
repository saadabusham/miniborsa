package com.technzone.miniborsa.ui.business.createbusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.enums.LocaleImageType
import com.technzone.miniborsa.data.models.createbusiness.LocaleImage
import com.technzone.miniborsa.databinding.RowAddFirstImageBinding
import com.technzone.miniborsa.databinding.RowAddImageBinding
import com.technzone.miniborsa.databinding.RowImageViewBinding
import com.technzone.miniborsa.databinding.RowMainImageViewBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class ImageRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<LocaleImage>(context) {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && items[position].contentType == LocaleImageType.IMAGE) LocaleImageType.MAIN_IMAGE.value else {
            items[position].contentType?.value ?: LocaleImageType.ADD_IMAGE.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LocaleImageType.MAIN_IMAGE.value -> MainImageViewHolder(
                RowMainImageViewBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
            LocaleImageType.IMAGE.value -> ImageViewHolder(
                RowImageViewBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
            LocaleImageType.ADD_FIRST.value -> AddFirstImageViewHolder(
                RowAddFirstImageBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
            else -> AddImageViewHolder(
                RowAddImageBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainImageViewHolder -> {
                holder.bind(items[position])
            }
            is ImageViewHolder -> {
                holder.bind(items[position])
            }
            is AddImageViewHolder -> {
                holder.bind(items[position])
            }
            is AddFirstImageViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    inner class MainImageViewHolder(private val binding: RowMainImageViewBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.item = item
            binding.imgRemove.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.relativePreview.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.relativePreview.setOnLongClickListener {
                itemClickListener?.onItemLongClick(it, bindingAdapterPosition, item)
                return@setOnLongClickListener true
            }
        }
    }

    inner class ImageViewHolder(private val binding: RowImageViewBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.item = item
            binding.imgRemove.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.relativePreview.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.relativePreview.setOnLongClickListener {
                itemClickListener?.onItemLongClick(it, bindingAdapterPosition, item)
                return@setOnLongClickListener true
            }
        }
    }

    inner class AddImageViewHolder(private val binding: RowAddImageBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

    inner class AddFirstImageViewHolder(private val binding: RowAddFirstImageBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.uploadImagesBtn.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

}