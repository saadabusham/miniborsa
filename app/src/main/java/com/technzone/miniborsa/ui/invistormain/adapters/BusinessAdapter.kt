package com.technzone.miniborsa.ui.invistormain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.databinding.RowBusinessBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class BusinessAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Business>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessBinding) :
        BaseViewHolder<Business>(binding.root) {

        override fun bind(item: Business) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }


}