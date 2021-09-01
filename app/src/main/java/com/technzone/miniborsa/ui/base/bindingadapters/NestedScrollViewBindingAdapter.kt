package com.technzone.miniborsa.ui.base.bindingadapters

import android.view.View
import androidx.annotation.IdRes
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter(
    value = ["nsvOnScrollChange", "nsvShouldHideFabOnRVScroll", "nsvFabView"],
    requireAll = false
)
fun NestedScrollView?.setOnScrollListener(
    onScrollChange: OnScrollChange?,
    shouldHideFabOnRVScroll: Boolean = false,
    @IdRes nsvFabView: Int
) {

    val fabView: FloatingActionButton? = (this?.parent as View).findViewById(nsvFabView)

    this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
    { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        onScrollChange?.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY)

        if (shouldHideFabOnRVScroll) {
            if (scrollY > oldScrollY) {
                fabView?.hide()
            } else {
                fabView?.show()
            }
        }
    })
}

interface OnScrollChange {
    fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    )
}
