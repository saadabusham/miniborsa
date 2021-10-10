package com.technzone.minibursa.ui.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.subscrption.Subscription
import com.technzone.minibursa.databinding.RowInvestorSubscriptionBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class InvestorSubscriptionRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<Subscription>(context) {

    fun getSelectedItem(): Subscription? {
        return items.singleOrNull { it.selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowInvestorSubscriptionBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowInvestorSubscriptionBinding) :
        BaseViewHolder<Subscription>(binding.root) {

        override fun bind(item: Subscription) {
            binding.item = item
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull { it.value.selected }?.let {
                    it.value.selected = false
                    notifyItemChanged(it.index)
                }
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it, absoluteAdapterPosition, item)
            }
        }
    }

}