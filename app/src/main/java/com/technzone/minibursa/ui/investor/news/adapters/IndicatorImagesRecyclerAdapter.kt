package com.technzone.minibursa.ui.investor.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.databinding.RowIndicatorImageBinding
import com.technzone.minibursa.databinding.RowIndicatorImageSmallBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder

class IndicatorImagesRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Boolean>(context) {
    companion object {
        const val NORMAL = 1
        const val SMALL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.get(position)) NORMAL else SMALL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NORMAL -> {
                NormalViewHolder(
                    RowIndicatorImageBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }
            else -> {
                SmallViewHolder(
                    RowIndicatorImageSmallBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.bind(items[position])
        } else if (holder is SmallViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class NormalViewHolder(private val binding: RowIndicatorImageBinding) :
        BaseViewHolder<Boolean>(binding.root) {

        override fun bind(item: Boolean) {

        }
    }

    inner class SmallViewHolder(private val binding: RowIndicatorImageSmallBinding) :
        BaseViewHolder<Boolean>(binding.root) {

        override fun bind(item: Boolean) {
        }
    }
}