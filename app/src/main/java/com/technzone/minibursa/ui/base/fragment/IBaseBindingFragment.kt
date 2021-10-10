package com.technzone.minibursa.ui.base.fragment

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum

interface IBaseBindingFragment {

    fun getLayoutId(): Int

    /** Called JUST  first time draw view OR when shouldRefreshPageWhenReturn == true**/
    fun onViewVisible() {}

    fun hasNavigation(): Boolean = true

    fun shouldRefreshPageWhenReturn(): Boolean = false

    /**
     * This Method Used TO Show Progress Dialog For Loading
     * Note Should Used JUST when you need to block user interacting with app
     */
    fun showLoadingView()

    fun hideLoadingView()

    fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage:String?
    )

    fun showProgressDialog(
        title: String = "",
        message: String,
        isRemovable: Boolean = false
    )

    fun isBindingEnabled(): Boolean = true

    fun addToolbar(
        toolbarView: Toolbar? = null,
        hasToolbar: Boolean,
        hasBackButton: Boolean,
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

    fun onAfterLocaleChanged()
}