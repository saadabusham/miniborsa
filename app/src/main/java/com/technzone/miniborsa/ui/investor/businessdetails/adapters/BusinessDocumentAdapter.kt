package com.technzone.miniborsa.ui.investor.businessdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.Field
import com.technzone.miniborsa.data.models.investor.FieldsItem
import com.technzone.miniborsa.databinding.RowBusinessDetailsBinding
import com.technzone.miniborsa.databinding.RowBusinessDocumentBinding
import com.technzone.miniborsa.databinding.RowBusinessImageBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class BusinessDocumentAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Media>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessDocumentBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessDocumentBinding) :
        BaseViewHolder<Media>(binding.root) {

        override fun bind(item: Media) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }


}