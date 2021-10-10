package com.technzone.minibursa.ui.base.bindingadapters

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.R

@BindingAdapter("rvItems")
fun <MODEL> RecyclerView?.setItems(items: List<MODEL>?) {
    if (this?.adapter is BaseBindingRecyclerViewAdapter<*> && adapter != null && items != null) {
        (adapter as BaseBindingRecyclerViewAdapter<MODEL>).submitItems(items)
    }
}

@BindingAdapter("rvItems")
fun <MODEL> RecyclerView?.setItems(items: LiveData<List<MODEL>>?) {
    if (this?.adapter is BaseBindingRecyclerViewAdapter<*> && adapter != null && items != null && items.value != null) {
        (adapter as BaseBindingRecyclerViewAdapter<MODEL>).submitItems(items.value!!)
    }
}

@BindingAdapter("rvSetOnItemClickListener")
fun RecyclerView?.setOnItemClickListener(
    onItemClickListener: BaseBindingRecyclerViewAdapter.OnItemClickListener?
) {
    this?.adapter?.let {
        if (this.adapter is BaseBindingRecyclerViewAdapter<*>) {
            (adapter as BaseBindingRecyclerViewAdapter<*>).itemClickListener = onItemClickListener
        }
    }

}

@BindingAdapter(
    value = ["onScrollStateChanged", "onScrolled", "shouldHideFabOnRVScroll", "rvFabView"],
    requireAll = false
)
fun RecyclerView?.setOnScrollListener(
    scrollStateChanged: OnScrollStateChanged?,
    scrolled: OnScrolled?,
    shouldHideFabOnRVScroll: Boolean = false,
    @IdRes rvFabView: Int
) {

    val fabView: FloatingActionButton? = (this?.parent as View).findViewById(rvFabView)

    val newValue: RecyclerView.OnScrollListener?
    newValue =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                scrollStateChanged?.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    scrolled?.onScrolled(recyclerView, dx, dy)
                }

                if (shouldHideFabOnRVScroll) {
                    if (dy > 0) {
                        fabView?.hide()
                    } else {
                        fabView?.show()
                    }
                }

            }
        }

    val oldValue =
        ListenerUtil.trackListener(
            this, newValue,
            R.id.recyclerViewOnScrollListener
        )

    if (oldValue != null) {
        this.removeOnScrollListener(oldValue)
    }
    this.addOnScrollListener(newValue)
}

interface OnScrollStateChanged {
    fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int)
}

interface OnScrolled {
    fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int)
}