package com.technzone.miniborsa.ui.base.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter

@BindingAdapter("vp2Items")
fun <MODEL> ViewPager2?.setItems(items: List<MODEL>?) {
    if (this?.adapter is BaseBindingRecyclerViewAdapter<*> && adapter != null && items != null) {
        items.let {
            (adapter as BaseBindingRecyclerViewAdapter<MODEL>).submitItems(it)
        }
    }
}

@BindingAdapter("vp2SetOnItemClickListener")
fun ViewPager2?.setOnItemClickListener(
    onItemClickListener: BaseBindingRecyclerViewAdapter.OnItemClickListener?
) {
    this?.adapter?.let {
        if (this.adapter is BaseBindingRecyclerViewAdapter<*>) {
            (adapter as BaseBindingRecyclerViewAdapter<*>).itemClickListener = onItemClickListener
        }
    }

}