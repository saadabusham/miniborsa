package com.technzone.minibursa.ui.business.createbusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.enums.LocaleImageType
import com.technzone.minibursa.data.models.createbusiness.LocaleImage
import com.technzone.minibursa.databinding.RowAddDocumentBinding
import com.technzone.minibursa.databinding.RowAddFirstDocumentBinding
import com.technzone.minibursa.databinding.RowDocumentBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class DocumentsRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<LocaleImage>(context) {


    companion object {
        const val ADD_FIRST_VIEW_TYPE = 1
        const val ADD_VIEW_TYPE = 2
        const val FILE_VIEW_TYPE = 3
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].contentType?.value ?: LocaleImageType.ADD_IMAGE.value
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LocaleImageType.ADD_FIRST.value -> AddFirstDocumentViewHolder(
                RowAddFirstDocumentBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
            LocaleImageType.IMAGE.value -> DocumentViewHolder(
                RowDocumentBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
            else -> AddDocumentViewHolder(
                RowAddDocumentBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DocumentViewHolder -> {
                holder.bind(items[position])
            }
            is AddDocumentViewHolder -> {
                holder.bind(items[position])
            }
            is AddFirstDocumentViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    inner class DocumentViewHolder(private val binding: RowDocumentBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.document = item
            binding.imgRemove.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

    inner class AddDocumentViewHolder(private val binding: RowAddDocumentBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

    inner class AddFirstDocumentViewHolder(private val binding: RowAddFirstDocumentBinding) :
        BaseViewHolder<LocaleImage>(binding.root) {

        override fun bind(item: LocaleImage) {
            binding.btnUploadDocuments.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

}