package com.technzone.miniborsa.ui.general.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.RowSearchGeneralBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class ChooseGeneralRecyclerAdapter constructor(
        context: Context
) : BaseBindingRecyclerViewAdapter<GeneralLookup>(context) {

    fun getSelectedItem(): GeneralLookup? {
        return items.singleOrNull { it.selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowSearchGeneralBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowSearchGeneralBinding) :
            BaseViewHolder<GeneralLookup>(binding.root) {

        override fun bind(item: GeneralLookup) {
            binding.item = item
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                itemClickListener?.onItemChecked(isChecked,item,bindingAdapterPosition)
                item.selected = isChecked
            }
        }
    }

    fun checkItem(item: GeneralLookup?) {
        items.forEachIndexed { index, symptom ->
            if (symptom.id == item?.id) {
                symptom.selected = true
                notifyItemChanged(index)
            }
        }
    }

    fun unCheckItem(item: GeneralLookup?) {
        items.forEachIndexed { index, symptom ->
            if (symptom.id == item?.id) {
                symptom.selected = false
                notifyItemChanged(index)
            }
        }
    }
}