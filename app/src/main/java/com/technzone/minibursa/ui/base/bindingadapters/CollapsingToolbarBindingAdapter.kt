package com.technzone.minibursa.ui.base.bindingadapters

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs

@BindingAdapter(
    value = ["ctlShowTitleJustWhenCollapsed", "ctlAppBar", "ctlTitle"],
    requireAll = true
)
fun CollapsingToolbarLayout?.showTitleJustWhenCollapsed(
    showTitleWhenCollapsed: Boolean,
    @IdRes appBarId: Int,
    title: String?
) {
    if (!showTitleWhenCollapsed) {
        return
    }

    var isShow = true
    var scrollRange = -1
    val appBarLayout: AppBarLayout? = (this?.parent as View).findViewById(appBarId)

    appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
        if (scrollRange == -1) {
            scrollRange = barLayout?.totalScrollRange!!
        }
        if (scrollRange + verticalOffset == 0) {
            this.title = title
            isShow = true
        } else if (isShow) {
            this.title =
                " " //careful there should a space between double quote otherwise it wont work
            isShow = false
        }
    })
}

@BindingAdapter(
    value = ["ctlToolbar", "ctlCollapsedColor", "ctlExpandedColor", "changeToolbarNavigationIconColor"],
    requireAll = true
)
fun AppBarLayout?.changeToolbarTitleAndIconColorOnCollapsingChanged(
    @IdRes toolbarId: Int,
    @ColorRes collapsedColor: Int,
    @ColorRes expandedColor: Int,
    changeNavigationIconColor: Boolean
) {
    var isShow = true
    var scrollRange = -1
    val toolbar: Toolbar = (this?.parent as View).findViewById(toolbarId)

    this.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
        if (scrollRange == -1) {
            scrollRange = barLayout?.totalScrollRange!!
        }
        if (scrollRange + verticalOffset == 0) {
            toolbar.setTitleTextColor(resources.getColor(collapsedColor))
            if (changeNavigationIconColor) {
                toolbar.navigationIcon?.setTint(resources.getColor(collapsedColor))
            }
            isShow = true
        } else if (isShow) {
            toolbar.setTitleTextColor(resources.getColor(expandedColor))
            if (changeNavigationIconColor) {
                toolbar.navigationIcon?.setTint(resources.getColor(expandedColor))
            }
            isShow = false
        }
    })
}


@BindingAdapter(
    value = ["ctlToolbarToChangeRes",
        "ctlCollapsedRes",
        "ctlExpandedRes",
        "changeToolbarNavigationIconRes"],
    requireAll = true
)
fun AppBarLayout?.changeToolbarIconREsOnCollapsingChanged(
    @IdRes toolbarId: Int,
    @DrawableRes collapsedRes: Int,
    @DrawableRes expandedRes: Int,
    changeNavigationIconRes: Boolean
) {
    var isShow = true
    var scrollRange = -1
    val toolbar: Toolbar = (this?.parent as View).findViewById(toolbarId)

    this.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
        if (scrollRange == -1) {
            scrollRange = barLayout?.totalScrollRange!!
        }
        if (scrollRange + verticalOffset == 0) {
            if (changeNavigationIconRes) {
                toolbar.navigationIcon = (resources.getDrawable(collapsedRes))
            }
            isShow = true
        } else if (isShow) {
            if (changeNavigationIconRes) {
                toolbar.navigationIcon = (resources.getDrawable(expandedRes))
            }
            isShow = false
        }
    })
}

@BindingAdapter(
    value = ["ctlLayoutToShow", "nestedScroll"],
    requireAll = true
)
fun AppBarLayout?.showViewOnCollapsed(
    @IdRes layoutId: Int,
    @IdRes nestedScrollId: Int
) {
    val view: View = (this?.parent as View).findViewById(layoutId)
    val nestedScroll: View = (this?.parent as View).findViewById(nestedScrollId)

    this.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
        view.alpha = abs(verticalOffset / barLayout.totalScrollRange.toFloat())
        val params = nestedScroll.layoutParams as CoordinatorLayout.LayoutParams
        params.topMargin =
            when(verticalOffset){
                0 -> -60
                else -> (((((abs(verticalOffset / barLayout.totalScrollRange.toFloat())) / 2) + 0.1) * 100) - 60).toInt()
            }
        nestedScroll.layoutParams = params
    })
}