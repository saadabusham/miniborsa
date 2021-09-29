package com.technzone.miniborsa.ui.investor.invistormain.fragment.investorfavorite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.databinding.RowBusinessBinding
import com.technzone.miniborsa.databinding.RowFavoriteBusinessBinding
import com.technzone.miniborsa.databinding.RowSearchedBusinessBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class FavoritesAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Business>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowFavoriteBusinessBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowFavoriteBusinessBinding) :
        BaseViewHolder<Business>(binding.root) {

        override fun bind(item: Business) {
            binding.item = item
            binding.favorite = item.isFavorite
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.imgFavorite.setOnClickListener {
                item.isFavorite =
                    item.isFavorite == false
                binding.favorite = item.isFavorite
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}