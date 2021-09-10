package com.technzone.miniborsa.ui.investor.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.RowNewsTabBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class TabNewsRecyclerAdapter @Inject constructor(
    @ActivityContext context: Context
) : BaseBindingRecyclerViewAdapter<GeneralLookup>(context) {

    fun getSelectedItem(): GeneralLookup? {
        return items.singleOrNull { it.isSelected.value ?: false }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowNewsTabBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowNewsTabBinding) :
        BaseViewHolder<GeneralLookup>(binding.root) {

        override fun bind(item: GeneralLookup) {
            binding.data = item
            binding.lifecycleOwner = context as AppCompatActivity
            binding.root.setOnClickListener {
                items.withIndex()
                    .singleOrNull { (i, value) -> value.isSelected.value!! }
                    ?.let {
                        if (bindingAdapterPosition == it.index)
                            return@let
                        it.value.isSelected.value = (false)
                    }
                item.isSelected.value = (true)
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }
}