package com.technzone.minibursa.ui.business.listingpreview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.business.ListingItem
import com.technzone.minibursa.databinding.RowListingItemBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class ListingItemAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<ListingItem>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowListingItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowListingItemBinding) :
        BaseViewHolder<ListingItem>(binding.root) {

        override fun bind(item: ListingItem) {
            binding.item = item
        }
    }


}