package com.technzone.minibursa.ui.base.bindingadapters

import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView


@BindingAdapter("cvBackgroundRes")
fun CardView?.setBackgroundRes(@DrawableRes background: Int) {
    this?.setBackgroundResource(background)
}

@BindingAdapter("cvIsChecked")
fun MaterialCardView?.setIsChecked(isChecked: Boolean) {
    this?.isChecked = isChecked
}