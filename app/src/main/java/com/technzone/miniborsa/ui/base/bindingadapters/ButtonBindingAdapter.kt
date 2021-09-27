package com.technzone.miniborsa.ui.base.bindingadapters

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.BusinessStatusEnums
import com.technzone.miniborsa.ui.base.views.AppButton

@BindingAdapter("setButtonDataByStatus")
fun AppButton.setButtonDataByStatus(
    status:Int
) {
    when(status){
        BusinessStatusEnums.DRAFT.value -> {
            this.text = context.getString(R.string.remove_draft)
            backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.status_draft))
        }
        BusinessStatusEnums.NEW.value -> {
            this.text = context.getString(R.string.pending_for_review)
            backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.status_pending))
            this.icon = resources.getDrawable(R.drawable.ic_pending)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
        BusinessStatusEnums.APPROVED.value -> {
            this.text = context.getString(R.string.approved_business)
            backgroundTintList = ContextCompat.getColorStateList(context,R.color.status_approved)
            this.icon = resources.getDrawable(R.drawable.ic_approved)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
        BusinessStatusEnums.REJECTED.value -> {
            this.text = context.getString(R.string.rejected_business)
            backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.status_rejected))
            this.icon = resources.getDrawable(R.drawable.ic_rejected)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
    }
}