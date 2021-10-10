package com.technzone.minibursa.ui.auth.onboarding.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.minibursa.databinding.RowOnBoardingBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.adapters.BaseViewHolder
import com.technzone.minibursa.data.models.auth.onboarding.OnBoardingItem

class OnBoardingAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<OnBoardingItem>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowOnBoardingBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowOnBoardingBinding) :
        BaseViewHolder<OnBoardingItem>(binding.root) {

        override fun bind(item: OnBoardingItem) {
            binding.data = item
        }
    }


}