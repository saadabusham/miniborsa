package com.technzone.minibursa.ui.base.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingRecyclerViewAdapter<MODEL>(
    protected val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<MODEL> = ArrayList()
    var itemClickListener: OnItemClickListener? = null


    fun submitNewItems(list: List<MODEL>?) {
        items = ArrayList()
        list?.let {
            items.addAll(it)
            notifyDataSetChanged()
        }

    }

    fun submitItems(newItems: List<MODEL>) {
        if (items == newItems) {
            return
        }

        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun submitItemsToTop(newItems: List<MODEL>) {
        items.addAll(0,newItems)
        notifyDataSetChanged()
    }
    fun submitItemToTop(newItem: MODEL) {
        items.add(0,newItem)
        notifyDataSetChanged()
    }
    fun submitItemToPosition(newItem: MODEL,position: Int) {
        items.add(position,newItem)
        notifyDataSetChanged()
    }

    fun addItems(list: List<MODEL>?) {
        val oldItems = items
        list?.let {
            items.addAll(it)
            notifyItemRangeChanged(oldItems.size, list.size)
        }

    }

    fun submitItem(newItem: MODEL) {
        items.add(newItem)
        notifyDataSetChanged()
    }

    fun clear(){
        items=ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, item: Any)
        fun onItemLongClick(view: View?, position: Int, item: Any){}
        fun onItemChecked(isChecked: Boolean? = null, item: Any, position: Int){}
    }
}