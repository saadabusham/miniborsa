package com.technzone.miniborsa.common.interfaces

import android.widget.SeekBar

interface SeekbarCallback : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(fromUser)
            onFromUserChange(progress)
        else
            onAutoChange(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
    fun onFromUserChange(progress: Int)
    fun onAutoChange(progress: Int){}
}