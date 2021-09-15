package com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.databinding.RowBusinessExtraInfoBinding
import com.technzone.miniborsa.databinding.RowListingBinding
import com.technzone.miniborsa.databinding.RowListingPendingBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class ListingAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<OwnerBusiness>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowListingBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowListingBinding) :
        BaseViewHolder<OwnerBusiness>(binding.root) {

        override fun bind(item: OwnerBusiness) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}