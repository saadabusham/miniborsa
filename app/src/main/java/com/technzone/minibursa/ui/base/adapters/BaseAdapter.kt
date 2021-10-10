package com.technzone.alalami.ui.base.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<M, VH : RecyclerView.ViewHolder>(var items: ArrayList<M> = ArrayList()) :
    RecyclerView.Adapter<VH>() {

    var onItemClickListener: OnItemClickListener<M>? = null

    fun submitItems(list: List<M>?) {
        items = ArrayList()
        list?.let {
            items.addAll(it)
            notifyDataSetChanged()
        }

    }

    fun remove(item: M) {
        val position = items.indexOf(item)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addItems(list: List<M>?) {
        val oldItems = items
        list?.let {
            items.addAll(it)
            notifyItemRangeChanged(oldItems.size,list.size)
        }

    }

    fun clear() {
        if (items.count() > 0) {
            items = ArrayList()
            notifyDataSetChanged()
        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener<M>) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

interface OnItemClickListener<T> {
    fun onItemClicked(view: View? = null, item: T, position: Int)
}