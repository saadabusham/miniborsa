package com.technzone.minibursa.ui.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.plan.Plan
import com.technzone.minibursa.databinding.RowBusinessSubscriptionBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class BusinessSubscriptionRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<Plan>(context) {

    fun getSelectedItem(): Plan? {
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
        BaseViewHolder<Plan>(binding.root) {

        override fun bind(item: Plan) {
            binding.item = item
//            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//                itemClickListener?.onItemChecked(isChecked, item, bindingAdapterPosition)
//                item.promote = isChecked
//                if(isChecked){
//                    binding.root.callOnClick()
//                }
//            }
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull { it.value.selected }?.let {
                    if (it.index != bindingAdapterPosition) {
                        it.value.selected = false
//                        it.value.promote = false
                        notifyItemChanged(it.index)
                    }
                }
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

}