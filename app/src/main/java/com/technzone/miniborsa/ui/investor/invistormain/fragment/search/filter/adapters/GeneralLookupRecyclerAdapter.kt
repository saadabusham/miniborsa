package com.technzone.miniborsa.ui.investor.invistormain.fragment.search.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.RowGeneralBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class GeneralLookupRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<GeneralLookup>(context) {

    fun getSelectedItem(): GeneralLookup? {
        return items.singleOrNull { it.selected }
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
                item.selected = item.selected == false
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }
}