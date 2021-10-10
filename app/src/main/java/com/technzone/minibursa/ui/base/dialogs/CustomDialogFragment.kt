package com.technzone.minibursa.ui.base.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.technzone.minibursa.R

class CustomDialogFragment private constructor(dialogBuilder: DialogBuilder) :
    DialogFragment() {

    private var mContext: Context
    private var title: String? = null
    private var message: String? = null
    private var positiveBtn: String? = null
    private var negativeBtn: String? = null
    private var neutralBtn: String? = null
    private var listener: CustomDialogListeners? = null
    private var customLayoutResID: Int = -1
    private var contentView: View? = null
    private var autoDismiss: Boolean = false
    private var mIsCancelable: Boolean = false


    init {
        mIsCancelable = dialogBuilder.isCancelable
        mContext = dialogBuilder.context
        mContext = dialogBuilder.context
        title = dialogBuilder.title
        message = dialogBuilder.message
        positiveBtn = dialogBuilder.positiveBtn
        negativeBtn = dialogBuilder.negativeBtn
        neutralBtn = dialogBuilder.neutralBtn
        listener = dialogBuilder.listener
        customLayoutResID = dialogBuilder.customLayoutResID
        contentView = dialogBuilder.contentView
        autoDismiss = dialogBuilder.autoDismiss
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener?.onBuildListener(context)

        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext, R.style.AlertDialogTheme)

        if (contentView != null) {
            builder.setView(contentView)
        }

        if (customLayoutResID != -1) {
            builder.setView(customLayoutResID)
        }

        builder.setMessage(message)

        val titleTextView = TextView(requireContext())
        titleTextView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.edit_text_text_color
            )
        )
        titleTextView.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            resources.getDimension(R.dimen._16ssp)
        )
        titleTextView.setTextAppearance(context, R.style.normalMediumFontStyle)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, 20, 0, 0)
        titleTextView.setPadding(0, 30, 0, 0)
        titleTextView.layoutParams = lp
        titleTextView.text = title
        titleTextView.gravity = Gravity.CENTER

        builder.setCustomTitle(titleTextView)

        if (positiveBtn != null) {
            builder.setPositiveButton(
                positiveBtn,
                if (!autoDismiss) DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    if (listener != null) {
                        listener!!.onPositiveClickListener(this)
                    }
                } else null
            )
        }


        if (negativeBtn != null) {
            builder.setNegativeButton(
                negativeBtn,
                if (!autoDismiss) DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    if (listener != null) {
                        listener!!.onNegativeClickListener(this)
                    }
                } else null
            )
        }

        if (negativeBtn != null) {
            builder.setNeutralButton(
                neutralBtn,
                if (!autoDismiss) DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    if (listener != null) {
                        listener!!.onNeutralClickListener(this)
                    }
                } else null
            )
        }

        val alertDialog = builder.create()
        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimations

        isCancelable = mIsCancelable //disable back button

        alertDialog.setCanceledOnTouchOutside(mIsCancelable)

        alertDialog.window
            ?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.shape_dialog_bg
                )
            )

        alertDialog.setOnShowListener { listener?.onBuildDoneListener(alertDialog) }

        return alertDialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener?.onCancelListener(dialog)
    }

    class DialogBuilder(
        val context: Context,
        val title: String?,
        val message: String?,
        val positiveBtn: String? = null
    ) {
        var negativeBtn: String? = null
            private set
        var neutralBtn: String? = null
            private set
        var isCancelable = false
            private set
        var listener: CustomDialogListeners? = null
            private set
        var customLayoutResID = 0
            private set
        var contentView: View? = null
            private set
        var autoDismiss = false

        fun setNegativeBtn(negativeBtn: String?): DialogBuilder {
            this.negativeBtn = negativeBtn
            return this
        }

        fun setNeutralBtn(neutralBtn: String?): DialogBuilder {
            this.neutralBtn = neutralBtn
            return this
        }

        fun setIsCancelable(isCancelable: Boolean): DialogBuilder {
            this.isCancelable = isCancelable
            return this
        }

        fun setListener(listener: CustomDialogListeners?): DialogBuilder {
            this.listener = listener
            return this
        }

        fun setCustomLayoutResID(customLayoutResID: Int): DialogBuilder {
            this.customLayoutResID = customLayoutResID
            return this
        }

        fun setContentView(contentView: View?): DialogBuilder {
            this.contentView = contentView
            return this
        }

        fun setAutoDismiss(autoDismiss: Boolean): DialogBuilder {
            this.autoDismiss = autoDismiss
            return this
        }

        fun build(): CustomDialogFragment = CustomDialogFragment(this)
    }
}