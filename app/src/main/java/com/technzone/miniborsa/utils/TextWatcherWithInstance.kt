package com.technzone.miniborsa.utils

import android.text.Editable
import android.widget.EditText

 interface TextWatcherWithInstance {
    fun beforeTextChanged(
        editText: EditText?,
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    )

    fun onTextChanged(
        editText: EditText?,
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    )

    fun afterTextChanged(editText: EditText?, editable: Editable?)
}