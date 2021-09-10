package com.technzone.miniborsa.ui.countrypicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.general.Countries
import com.technzone.miniborsa.databinding.RowCountryBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible

class CountriesRecyclerAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<Countries>(context) {

    var selectedPosition: Int = -1

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
                if (selectedPosition != -1) {
                    items[selectedPosition].selected = false
                    notifyItemChanged(selectedPosition)
                }
                selectedPosition = bindingAdapterPosition
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it,bindingAdapterPosition,item)
            }
            if (item.selected) {
                binding.imgSelected.visible()
            } else {
                binding.imgSelected.gone()
            }
        }
    }
}