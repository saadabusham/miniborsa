package com.technzone.minibursa.ui.investor.recentviewed.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.databinding.RowRecentViewBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class RecentViewAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Business>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowRecentViewBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowRecentViewBinding) :
        BaseViewHolder<Business>(binding.root) {

        override fun bind(item: Business) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.favorite = item.isFavorite
            binding.imgFavorite.setOnClickListener {
                item.isFavorite =
                    item.isFavorite == false
                binding.favorite = item.isFavorite
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}