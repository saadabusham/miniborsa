package com.technzone.miniborsa.ui.base.bindingadapters

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.NewsSectionEnums
import com.technzone.miniborsa.data.enums.PropertyStatusEnums

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
    backgroundTintList = ContextCompat.getColorStateList(
        context,
        when (type) {
            NewsSectionEnums.START_UP_NEWS.value -> R.color.startup_news
            NewsSectionEnums.INVESTMENT.value -> R.color.investment
            else -> R.color.tips
        }
    )
}

@BindingAdapter("setTextByBusinessType")
fun TextView.setTextByBusinessType(
    type: Int
) {
    text = context.getString(
        when (type) {
            BusinessTypeEnums.BUSINESS_FOR_SALE.value -> R.string.businesses_for_sale
            BusinessTypeEnums.BUSINESS_FOR_SHARE.value -> R.string.share_for_sale
            else -> R.string.franchise
        }
    )
}

@BindingAdapter("setTextBySaleType")
fun TextView.setTextBySaleType(
    type: Int
) {
    text = context.getPropertyStatus(type)
}

fun Context.getPropertyStatus(type: Int): String {
    return getString(
        when (type) {
            PropertyStatusEnums.FREEHOLD.value -> R.string.freehold
            PropertyStatusEnums.LEASEHOLD.value -> R.string.leasehold
            else -> R.string.both
        }
    )
}