package com.technzone.miniborsa.ui.base.bindingadapters

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.NewsSectionEnums

@BindingAdapter("setTextBySection")
fun TextView.setTextBySection(
    type: Int
) {
    text = context.getString(
        when (type) {
            NewsSectionEnums.START_UP_NEWS.value -> R.string.startup_news
            NewsSectionEnums.INVESTMENT.value -> R.string.investment
            else -> R.string.tips
        }
    )
}
@BindingAdapter("setColorBySection")
fun TextView.setColorBySection(
    type: Int
) {
    backgroundTintList = ContextCompat.getColorStateList(context,
        when (type) {
            NewsSectionEnums.START_UP_NEWS.value -> R.color.startup_news
            NewsSectionEnums.INVESTMENT.value -> R.color.investment
            else -> R.color.tips
        }
    )
}