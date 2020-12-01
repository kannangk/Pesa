package finance.pesa.sdk.utils

import androidx.transition.Visibility
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.animation.Animator
import androidx.transition.TransitionValues

class CircularRevealTransition: Visibility(){
   override fun onAppear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator {
        val startRadius = 0
        val endRadius = Math.hypot(view.getWidth().toDouble(), view.getHeight().toDouble()).toInt()
        val reveal = ViewAnimationUtils.createCircularReveal(
            view,
            view.getWidth() / 2,
            view.getHeight() / 2,
            startRadius.toFloat(),
            endRadius.toFloat()
        )
        //make view invisible until animation actually starts
        view.setAlpha(0f)
        reveal.addListener(object : AnimatorListenerAdapter() {
           override fun onAnimationStart(animation: Animator) {
                view.setAlpha(1f)
            }
        })
        return reveal
    }

   override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator {
        val endRadius = 0
        val startRadius = Math.hypot(view.getWidth().toDouble(), view.getHeight().toDouble()).toInt()
        return ViewAnimationUtils.createCircularReveal(
            view,
            view.getWidth() / 2,
            view.getHeight() / 2,
            startRadius.toFloat(),
            endRadius.toFloat()
        )
    }
}