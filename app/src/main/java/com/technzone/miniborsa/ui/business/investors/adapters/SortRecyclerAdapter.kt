package com.technzone.miniborsa.ui.business.investors.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.SortModel
import com.technzone.miniborsa.databinding.RowSortBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class SortRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<SortModel>(context) {

    fun getSelectedItem():SortModel?{
        return items.singleOrNull { it.selected.value == true }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(
            RowSortBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class NormalViewHolder(private val binding: RowSortBinding) :
        BaseViewHolder<SortModel>(binding.root) {
        override fun bind(item: SortModel) {
            binding.item = item
            binding.lifecycleOwner = context as AppCompatActivity
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull{it.value.selected.value == true}?.let {
                    it.value.selected.value = false
                }
                item.selected.value = true
                itemClickListener?.onItemClick(binding.root, bindingAdapterPosition, item)
            }
        }
    }
}