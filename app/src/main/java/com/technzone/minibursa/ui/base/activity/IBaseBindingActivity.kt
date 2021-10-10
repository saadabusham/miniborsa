package com.technzone.minibursa.ui.base.activity

import android.content.pm.ActivityInfo
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum

interface IBaseBindingActivity {

    fun addToolbar(
        hasToolbar: Boolean,
        toolbarView: Toolbar? = null,
        hasBackButton: Boolean,
        @ColorRes backArrowTint: Int? = null,
        hasTitle: Boolean = false,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null,
        hasSubTitle: Boolean = false,
        @StringRes subTitle: Int = R.string.empty_string,
        showBackArrow: Boolean = false,
        @DrawableRes navigationIcon: Int? = null
    )

    fun updateToolbarTitle(
        hasTitle: Boolean = true,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null
    )

    fun updateToolbarTitle(
        hasTitle: Boolean = true,
        @StringRes title: Int = R.string.empty_string,
        titleString: String? = null,
        hasBackButton: Boolean,
        @ColorRes backArrowTint: Int? = null,
        showBackArrow: Boolean = false
    )

    fun updateToolbarSubTitle(
        hasSubTitle: Boolean = true,
        @StringRes subTitle: Int = R.string.empty_string,
        subTitleString: String? = null
    )

    fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    )

    fun isBindingEnabled(): Boolean = true

    fun screenOrientation(): Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    /**
     * This Method Used TO Show Progress Dialog For Loading
     * Note Should Used JUST when you need to block user interacting with app
     */
    fun showLoadingView()

    fun hideLoadingView()
}