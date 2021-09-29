package com.technzone.miniborsa.ui.investor.filter.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.RowGeneralBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class GeneralLookupRecyclerAdapter constructor(
    context: Context,
    private val singleSelection: Boolean = false
) : BaseBindingRecyclerViewAdapter<GeneralLookup>(context) {

    fun getSelectedItem(): GeneralLookup? {
        return items.singleOrNull { it.selected }
    }

    fun getSelectedItems(): List<GeneralLookup> {
        return items.filter { it.selected }
    }

    fun clearSelectedItems() {
        items.withIndex().forEach {
            if (it.value.selected) {
                it.value.selected = false
                notifyItemChanged(it.index)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowGeneralBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowGeneralBinding) :
        BaseViewHolder<GeneralLookup>(binding.root) {

        override fun bind(item: GeneralLookup) {
            binding.item = item
            binding.root.setOnClickListener {
                if (singleSelection) {
                    items.withIndex().singleOrNull { it.value.selected }?.let {
                        it.value.selected = false
                        notifyItemChanged(it.index)
                    }
                }
                item.selected = item.selected == false
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }
}