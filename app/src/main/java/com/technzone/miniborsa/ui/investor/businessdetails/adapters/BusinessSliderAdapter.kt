package com.technzone.miniborsa.ui.investor.businessdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.databinding.RowBusinessImageBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class BusinessSliderAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Media>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessImageBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessImageBinding) :
        BaseViewHolder<Media>(binding.root) {

        override fun bind(item: Media) {
            binding.data = item.name
            if(itemCount == 1){
                binding.root.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,context.resources.getDimension(R.dimen._180sdp).toInt()).apply {
                    marginEnd = context.resources.getDimension(R.dimen._10sdp).toInt()
                    bottomMargin = context.resources.getDimension(R.dimen._5sdp).toInt()
                    topMargin = context.resources.getDimension(R.dimen._5sdp).toInt()
                }
            }
            binding.imgPicture.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}