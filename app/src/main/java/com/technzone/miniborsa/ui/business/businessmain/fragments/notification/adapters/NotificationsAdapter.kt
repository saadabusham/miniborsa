package com.technzone.miniborsa.ui.business.businessmain.fragments.notification.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.notification.Notification
import com.technzone.miniborsa.databinding.RowBusinessExtraInfoBinding
import com.technzone.miniborsa.databinding.RowListingBinding
import com.technzone.miniborsa.databinding.RowListingPendingBinding
import com.technzone.miniborsa.databinding.RowNotifcationBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.adapters.BaseViewHolder

class NotificationsAdapter(
    context: Context
) :
    BaseBindingRecyclerViewAdapter<Notification>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowNotifcationBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowNotifcationBinding) :
        BaseViewHolder<Notification>(binding.root) {

        override fun bind(item: Notification) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }


}