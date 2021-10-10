package com.technzone.minibursa.ui.base.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import com.technzone.minibursa.R


class AppEditText @JvmOverloads constructor(
    context: Context,
    private var attrs: AttributeSet,
    defStyleAttr: Int = R.attr.editTextStyle
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        initAttrs()
    }
    private fun initAttrs() {
        if (gravity != Gravity.CENTER) {
            textAlignment = TEXT_ALIGNMENT_VIEW_START
        }
    }

}