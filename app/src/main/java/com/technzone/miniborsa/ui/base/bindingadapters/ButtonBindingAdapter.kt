package com.technzone.miniborsa.ui.base.bindingadapters

import android.animation.StateListAnimator
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.BusinessStatusEnums
import com.technzone.miniborsa.ui.base.views.AppButton
import org.jetbrains.anko.textColor

@BindingAdapter("setButtonDataByStatus")
fun AppButton.setButtonDataByStatus(
    status: Int
) {
    when (status) {
        BusinessStatusEnums.DRAFT.value -> {
            this.text = context.getString(R.string.remove_draft)
            this.textColor = resources.getColor(R.color.status_draft_text)
            backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.status_draft))
            this.stateListAnimator = StateListAnimator()
            this.elevation = resources.getDimension(R.dimen._1sdp)
            this.invalidate()
        }
        BusinessStatusEnums.NEW.value -> {
            this.text = context.getString(R.string.pending_for_review)
            this.textColor = resources.getColor(R.color.status_pending_text)
            backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.status_pending))
            this.icon = resources.getDrawable(R.drawable.ic_pending)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
        BusinessStatusEnums.APPROVED.value -> {
            this.text = context.getString(R.string.approved_business)
            this.textColor = resources.getColor(R.color.status_approved_text)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.status_approved)
            this.icon = resources.getDrawable(R.drawable.ic_approved)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
        BusinessStatusEnums.REJECTED.value -> {
            this.text = context.getString(R.string.rejected_business)
            this.textColor = resources.getColor(R.color.status_rejected_text)
            backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.status_rejected))
            this.icon = resources.getDrawable(R.drawable.ic_rejected)
            this.stateListAnimator = null
            this.isClickable = false
            this.isFocusable = false
        }
    }
}