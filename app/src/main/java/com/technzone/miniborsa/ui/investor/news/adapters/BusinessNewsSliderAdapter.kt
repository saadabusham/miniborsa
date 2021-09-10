package com.technzone.miniborsa.ui.investor.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.RowBusinessNewsBinding
import com.technzone.miniborsa.databinding.RowBusinessNewsSliderBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class BusinessNewsSliderAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<BusinessNews>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessNewsSliderBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessNewsSliderBinding) :
        BaseViewHolder<BusinessNews>(binding.root) {

        override fun bind(item: BusinessNews) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}