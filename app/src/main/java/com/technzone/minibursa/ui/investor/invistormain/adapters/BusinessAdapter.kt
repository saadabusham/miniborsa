package com.technzone.minibursa.ui.investor.invistormain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.R
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.databinding.RowBusinessBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class BusinessAdapter(
    context: Context,
    private val loggedIn: Boolean = true
) : BaseBindingRecyclerViewAdapter<Business>(context) {

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
            if (itemCount == 1) {
                binding.root.layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = context.resources.getDimension(R.dimen.layouts_margin_start).toInt()
                    bottomMargin = context.resources.getDimension(R.dimen._5sdp).toInt()
                    topMargin = context.resources.getDimension(R.dimen._5sdp).toInt()
                }
            }
            binding.favorite = item.isFavorite
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.imgFavorite.setOnClickListener {
                if (loggedIn) {
                    item.isFavorite =
                        item.isFavorite == false
                    binding.favorite = item.isFavorite
                }
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}