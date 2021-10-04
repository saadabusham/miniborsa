package com.technzone.miniborsa.ui.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.subscrption.Subscription
import com.technzone.miniborsa.databinding.RowBusinessSubscriptionBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class BusinessSubscriptionRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<Subscription>(context) {

    fun getSelectedItem(): Subscription? {
        return items.singleOrNull { it.selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessSubscriptionBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessSubscriptionBinding) :
        BaseViewHolder<Subscription>(binding.root) {

        override fun bind(item: Subscription) {
            binding.item = item
//            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//                itemClickListener?.onItemChecked(isChecked, item, bindingAdapterPosition)
//                item.promote = isChecked
//            }
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull { it.value.selected }?.let {
                    it.value.selected = false
                    it.value.promote = false
                    notifyItemChanged(it.index)
                }
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

}