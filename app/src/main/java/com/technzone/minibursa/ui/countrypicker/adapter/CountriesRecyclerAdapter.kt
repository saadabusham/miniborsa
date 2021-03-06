package com.technzone.minibursa.ui.countrypicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.general.Countries
import com.technzone.minibursa.databinding.RowCountryBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible

class CountriesRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Countries>(context) {

    fun getSelectedItem(): Countries? {
        return items.singleOrNull { it.selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowCountryBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowCountryBinding) :
        BaseViewHolder<Countries>(binding.root) {

        override fun bind(item: Countries) {
            binding.item = item
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull { it.value.selected }?.let {
                    it.value.selected = false
                    notifyItemChanged(it.index)
                }
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            if (item.selected) {
                binding.imgSelected.visible()
            } else {
                binding.imgSelected.gone()
            }
        }
    }
}