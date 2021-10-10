package com.technzone.minibursa.ui.business.createbusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.data.models.investor.ExtraInfo
import com.technzone.minibursa.databinding.RowBusinessExtraInfoBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class ExtraInfoAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<ExtraInfo>(context) {

    fun getSelected(): List<ExtraInfo> {
        return items.filter { it.selected.value == true }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBusinessExtraInfoBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowBusinessExtraInfoBinding) :
        BaseViewHolder<ExtraInfo>(binding.root) {

        override fun bind(item: ExtraInfo) {
            binding.item = item
            binding.lifecycleOwner = (context as AppCompatActivity)
            binding.root.setOnClickListener {
                item.selected.value = item.selected.value == false
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}