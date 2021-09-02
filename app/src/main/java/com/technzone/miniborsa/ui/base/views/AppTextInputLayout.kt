package com.technzone.miniborsa.ui.base.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.textfield.TextInputLayout

class AppTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.textInputStyle
) : TextInputLayout(context, attrs, defStyleAttr)