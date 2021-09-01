package com.technzone.miniborsa.ui.base.bindingadapters

import android.content.res.Resources
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.databinding.BindingAdapter

@BindingAdapter("tpSetInterval")
fun TimePicker?.setTimePickerInterval(
    interval: Int
) {

    try {
        val minutePicker = this?.findViewById<NumberPicker>(
            Resources.getSystem().getIdentifier(
                "minute", "id", "android"
            )
        )
        minutePicker?.minValue = 0
        minutePicker?.maxValue = 60 / interval - 1
        val displayedValues: MutableList<String> =
            ArrayList()
        var i = 0
        while (i < 60) {
            displayedValues.add(String.format("%02d", i))
            i += interval
        }
        minutePicker?.displayedValues = displayedValues.toTypedArray()
    } catch (ignore: Exception) {

    }
}

fun TimePicker?.setIntervalChangeListener(
    interval: Int,
    changeListener: TimePicker.OnTimeChangedListener?
) {
    if (changeListener != null) {
        this?.setOnTimeChangedListener { view, hourOfDay, minute ->
            changeListener.onTimeChanged(view, hourOfDay, minute * interval)
        }
    }
}
