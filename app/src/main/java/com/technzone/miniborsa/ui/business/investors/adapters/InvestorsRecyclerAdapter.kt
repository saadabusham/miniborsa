package com.technzone.miniborsa.ui.business.investors.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.databinding.RowInvestorBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class InvestorsRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<Investor>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(
            RowInvestorBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class NormalViewHolder(private val binding: RowInvestorBinding) :
        BaseViewHolder<Investor>(binding.root) {
        override fun bind(item: Investor) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(binding.root, bindingAdapterPosition, item)
            }
        }
    }
}