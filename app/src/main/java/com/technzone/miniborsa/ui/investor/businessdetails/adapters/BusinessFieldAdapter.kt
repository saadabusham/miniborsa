package com.technzone.miniborsa.ui.investor.businessdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.investor.FieldsItem
import com.technzone.miniborsa.databinding.RowBusinessDetailsBinding
import com.technzone.miniborsa.databinding.RowBusinessImageBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder
import com.technzone.miniborsa.utils.extensions.visible

class BusinessFieldAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<GeneralLookup>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessDetailsBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessDetailsBinding) :
        BaseViewHolder<GeneralLookup>(binding.root) {

        override fun bind(item: GeneralLookup) {
            binding.item = item
            if(bindingAdapterPosition % 2 == 0){
                binding.divStart.visible()
            }
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}