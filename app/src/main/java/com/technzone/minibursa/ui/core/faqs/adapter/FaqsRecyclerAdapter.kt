package com.technzone.minibursa.ui.core.faqs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.FaqsResponse
import com.technzone.minibursa.databinding.RowFaqBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder
import com.technzone.minibursa.utils.extensions.rotate

class FaqsRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<FaqsResponse>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowFaqBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowFaqBinding) :
        BaseViewHolder<FaqsResponse>(binding.root) {

        override fun bind(item: FaqsResponse) {
            binding.root.setOnClickListener {
                if (!item.isExpanded) {
                    binding.tvDesc.maxLines = 1000
                    binding.ivArrow.rotate(true)
                } else {
                    binding.tvDesc.maxLines = 1
                    binding.ivArrow.rotate(false)
                }
                item.isExpanded = !item.isExpanded
            }
            binding.tvTitle.text = item.question
            binding.tvDesc.text = item.answer
        }
    }
}