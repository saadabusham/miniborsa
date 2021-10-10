package com.technzone.minibursa.utils.extensions

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.rotate(isDown: Boolean) {
    val rotate = RotateAnimation(
        if (isDown) 0.toFloat() else (-180).toFloat(),
        if (isDown) (-180).toFloat() else 0.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5f, Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotate.duration = 200
    rotate.fillAfter = true
    this.startAnimation(rotate)
}

fun ImageView.tint(color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}

