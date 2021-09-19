package com.technzone.miniborsa.ui.base.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.technzone.miniborsa.R
import java.util.*

object DialogsUtil {

    fun showDatePickerDialog(
        context: Context,
        calBefore: Calendar,
        listener: OnDateSetListener?,
        hasNegativeBtn: Boolean = true,
        @StringRes negativeBtnText: Int = R.string.cancel,
        @ColorRes negativeBtnTextColor: Int = R.color.text_default_color,
        hasPositiveBtn: Boolean = true,
        @StringRes positiveBtnText: Int = R.string.select,
        @ColorRes positiveBtnTextColor: Int = R.color.dark_blue,
        hasNeutralBtn: Boolean = false,
        @StringRes neutralBtnText: Int? = null,
        @ColorRes neutralBtnTextColor: Int = R.color.text_default_color,
        maxDate: Long? = Date().time,
        minDate: Long? = null,
        showDays: Boolean = true,
        showMonths: Boolean = true,
        showYears: Boolean = true
    ) {
        val dialog = DatePickerDialog(
            context,
            listener,
            calBefore.get(Calendar.YEAR),
            calBefore.get(Calendar.MONTH),
            calBefore.get(Calendar.DAY_OF_MONTH)
        )

        maxDate?.let {
            dialog.datePicker.maxDate = maxDate
        }

        minDate?.let {
            dialog.datePicker.minDate = minDate
        }

        dialog.show()

        handleHideDatePickerFields(dialog, showDays, showMonths, showYears)

        // set Up Negative Button
        val negativeBtn = dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        if (hasNegativeBtn) {
            negativeBtn.visibility = View.VISIBLE
            negativeBtn.setText(negativeBtnText)
            negativeBtn.setTextColor(ContextCompat.getColor(context, negativeBtnTextColor))
        } else {
            negativeBtn.visibility = View.GONE
        }

        // Set Up Positive Button
        val positiveBtn = dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        if (hasPositiveBtn) {
            positiveBtn.visibility = View.VISIBLE
            positiveBtn.setText(positiveBtnText)
            positiveBtn.setTextColor(ContextCompat.getColor(context, positiveBtnTextColor))
        } else {
            positiveBtn.visibility = View.GONE
        }

        // Set Up Neutral Button
        val neutralBtn = dialog.getButton(DatePickerDialog.BUTTON_NEUTRAL)
        if (hasNeutralBtn) {
            neutralBtn.visibility = View.VISIBLE
            if (neutralBtnText != null) {
                neutralBtn.setText(neutralBtnText)
            }
            neutralBtn.setTextColor(ContextCompat.getColor(context, neutralBtnTextColor))
        } else {
            neutralBtn.visibility = View.GONE
        }
    }


    private fun handleHideDatePickerFields(
        dialog: DatePickerDialog,
        showDays: Boolean,
        showMonths: Boolean,
        showYears: Boolean
    ) {
        if (!showDays) {

            val day: View? = dialog.findViewById(
                Resources.getSystem().getIdentifier("android:id/day", null, null)
            )
            day?.visibility = View.GONE
        }
        if (!showMonths) {
            val month: View? = dialog.findViewById(
                Resources.getSystem().getIdentifier("android:id/month", null, null)
            )
            month?.visibility = View.GONE
        }
        if (!showYears) {
            val year: View? = dialog.findViewById(
                Resources.getSystem().getIdentifier("android:id/year", null, null)
            )
            year?.visibility = View.GONE
        }
    }

    fun showDialog(
        context: Context,
        fragmentManager: FragmentManager,
        title: String? = "",
        message: String? = "",
        positiveBtn: String? = "",
        negativeBtn: String? = "",
        neutralBtn: String? = "",
        listener: CustomDialogListeners? = null,
        isCancelable: Boolean = false,
        layoutResID: Int = -1,
        contentView: View? = null,
        autoDismiss: Boolean = false
    ): CustomDialogFragment? {

        val dialog =
            CustomDialogFragment.DialogBuilder(context, title, message, positiveBtn)
                .setNegativeBtn(negativeBtn)
                .setNeutralBtn(neutralBtn)
                .setListener(listener)
                .setIsCancelable(isCancelable)
                .setCustomLayoutResID(layoutResID)
                .setContentView(contentView)
                .setAutoDismiss(autoDismiss)
                .build()
        dialog.show(fragmentManager, "null")
        return dialog
    }

    fun showTimePickerDialog(
        context: Context,
        dialogTitle: String?,
        listener: OnTimeSetListener?,
        is24HourView: Boolean = true,
        hasNegativeBtn: Boolean = true,
        @StringRes negativeBtnText: Int = R.string.cancel,
        @ColorRes negativeBtnTextColor: Int = R.color.text_default_color,
        hasPositiveBtn: Boolean = true,
        @StringRes positiveBtnText: Int = R.string.select,
        @ColorRes positiveBtnTextColor: Int = R.color.dark_blue
    ) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(context,R.style.themeOnverlay_timePicker, listener, hour, minute, is24HourView) //Yes 24 hour time
        mTimePicker.setTitle(dialogTitle)
        mTimePicker.show()

        // set Up Negative Button
        val negativeBtn = mTimePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        if (hasNegativeBtn) {
            negativeBtn.visibility = View.VISIBLE
            negativeBtn.setText(negativeBtnText)
            negativeBtn.setTextColor(ContextCompat.getColor(context, negativeBtnTextColor))
        } else {
            negativeBtn.visibility = View.GONE
        }

        // Set Up Positive Button
        val positiveBtn = mTimePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
        if (hasPositiveBtn) {
            positiveBtn.visibility = View.VISIBLE
            positiveBtn.setText(positiveBtnText)
            positiveBtn.setTextColor(ContextCompat.getColor(context, positiveBtnTextColor))
        } else {
            positiveBtn.visibility = View.GONE
        }
    }

    fun Activity.showLoginDialog(callBack:LoginDialog.CallBack){
        LoginDialog(this,callBack).show()
    }
}