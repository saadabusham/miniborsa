package com.technzone.minibursa.utils

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.transition.Transition
import androidx.transition.TransitionValues

class ColorTransition : Transition {
    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    override fun getTransitionProperties(): Array<String>? {
        return TRANSITION_PROPERTIES
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        this.captureValues(transitionValues)
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        this.captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        var color: Int
        if (transitionValues.view is TextView) {
            color = (transitionValues.view as TextView).currentTextColor
            transitionValues.values["textColorToTransition"] = color
        }
        if (transitionValues.view is ImageView) {
            color = transitionValues.view.solidColor
            transitionValues.values["textColorToTransition"] = color
        }
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        return if (startValues != null && endValues != null) {
            val startTextColor = startValues.values["textColorToTransition"] as Int?
            val endTextColor = endValues.values["textColorToTransition"] as Int?
            val textView = endValues.view as TextView
            val argbEvaluator = ArgbEvaluator()
            val animator = ValueAnimator.ofFloat(*floatArrayOf(0.0f, 1.0f))
            animator.addUpdateListener { animation ->
                val color = argbEvaluator.evaluate(
                    animation.animatedFraction,
                    startTextColor,
                    endTextColor
                ) as Int
                textView.setTextColor(color)
            }
            animator
        } else {
            null
        }
    }

    companion object {
        private val TRANSITION_PROPERTIES =
            arrayOf("textColorToTransition")
    }
}