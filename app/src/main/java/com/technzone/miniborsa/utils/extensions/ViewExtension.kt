package com.technzone.miniborsa.utils.extensions

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.technzone.miniborsa.BuildConfig
import java.lang.System.currentTimeMillis

fun View?.hideKeyboard(activity: Activity?) {
    val imm: InputMethodManager? =
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    //Find the currently focused view, so we can grab the correct window token from it.
    imm?.hideSoftInputFromWindow(this?.windowToken, 0)
}

fun View?.showPopupMenu(
    @MenuRes menu: Int,
    clickListener: PopupMenu.OnMenuItemClickListener? = null
) {
    this?.let {
        PopupMenu(this.context, it).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(clickListener)
            inflate(menu)
            show()
        }
    }
}

fun View.showSnackbar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, resources.getString(messageRes), length)
    snackBar.show()

}

fun View.showSnackbar(string: String, length: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, string, length)
    snackBar.show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.checkIfAllEditTextsFilled(): Boolean {
    val layout = this as ViewGroup
    var enable = false
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is EditText) {
            if (child.text.toString().isNotEmpty()) {
                enable = true
            }
        } else if (child is ViewGroup) {
            child.checkIfAllEditTextsFilled()
        }
    }
    return enable
}

fun View.addEditTextsWatcher(textWatcher: TextWatcher) {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is EditText) {
            child.addTextChangedListener(textWatcher)
        } else if (child is ViewGroup) {
            child.addEditTextsWatcher(textWatcher)
        }

    }
}


fun View.disableView() {
    isEnabled = false
}

fun View.enableView() {
    isEnabled = true
}


fun View.disableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = false
        if (child is ViewGroup) {
            child.disableViews()
        }
    }

}

fun View.enableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = true
        if (child is ViewGroup) {
            child.enableViews()
        }
    }
}


fun ImageView.loadImage(url: String?) {
    val fullUrl = when {
        url?.startsWith("/storage") == true -> {
            url
        }
        url?.startsWith("http") == false -> {
            IMAGES_BASE_URL + url
        }
        else -> {
            url
        }
    } as String

    Glide.with(this).load(fullUrl)
        .into(this)
}

inline fun EditText.onTextChanged(crossinline callback: (text: CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s)
        }
    })
}

fun View.onClick(click: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        if (currentTimeMillis() > lastClickTime + 550) click(this)
        lastClickTime = currentTimeMillis()
    }
}
const val IMAGES_BASE_URL = BuildConfig.ImageSubUrl