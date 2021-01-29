package com.lib.animated_menu.animation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


const val TRANSLATION_X_COEFFICIENT = 0.25f
const val TRANSLATION_Y_COEFFICIENT = 0.1f
const val SCALE_FACTOR = 0.9f

class AnimationHandler(private val listener: AnimationListener) {

    companion object {

        fun getOpenedStateAnimationProperties(view: View, cornerRadius: Int): AnimationProperties {
            val properties = AnimationProperties()
            properties.apply {
                this.x = view.width - view.width * TRANSLATION_X_COEFFICIENT
                this.y = view.height * TRANSLATION_Y_COEFFICIENT
                this.scale = SCALE_FACTOR
                this.corners = cornerRadius.toFloat()
            }
            return properties
        }

    }

    var isMenuOpened: Boolean = false

    private val animationProperties = AnimationProperties()

    fun openMenu(view: View, duration: Int, cornerRadius: Int) {
        isMenuOpened = true

        val animatorsSet = AnimatorSet()
        animatorsSet.interpolator = AccelerateDecelerateInterpolator()

        val scaleAnimator = ValueAnimator.ofFloat(animationProperties.scale, SCALE_FACTOR)
            .setDuration(duration.toLong())
        scaleAnimator.apply {
            this.addUpdateListener {
                animationProperties.scale = it.animatedValue as Float
            }
        }

        val xAnimator = ValueAnimator.ofFloat(
            animationProperties.x,
            view.width - view.width * TRANSLATION_X_COEFFICIENT
        )
            .setDuration(duration.toLong())
        xAnimator.apply {
            this.addUpdateListener {
                animationProperties.x = it.animatedValue as Float
            }
        }

        val yAnimator =
            ValueAnimator.ofFloat(animationProperties.y, view.height * TRANSLATION_Y_COEFFICIENT)
                .setDuration(duration.toLong())
        yAnimator.apply {
            this.addUpdateListener {
                animationProperties.y = it.animatedValue as Float
            }
        }

        val cornerAnimator =
            ValueAnimator.ofFloat(animationProperties.corners, cornerRadius.toFloat())
                .setDuration(duration.toLong())
        cornerAnimator.apply {
            this.addUpdateListener {
                animationProperties.corners = it.animatedValue as Float
                listener.onAnimation(animationProperties)
            }
        }

        animatorsSet.playTogether(listOf(scaleAnimator, xAnimator, yAnimator, cornerAnimator))
        animatorsSet.start()
    }

    fun closeMenu(view: View, duration: Int) {
        isMenuOpened = false

        val animatorsSet = AnimatorSet()
        animatorsSet.interpolator = AccelerateDecelerateInterpolator()

        val scaleAnimator = ValueAnimator.ofFloat(animationProperties.scale, 1f)
            .setDuration(duration.toLong())
        scaleAnimator.apply {
            this.addUpdateListener {
                animationProperties.scale = it.animatedValue as Float
            }
        }

        val xAnimator = ValueAnimator.ofFloat(animationProperties.x, 0f)
            .setDuration(duration.toLong())
        xAnimator.apply {
            this.addUpdateListener {
                animationProperties.x = it.animatedValue as Float
            }
        }

        val yAnimator = ValueAnimator.ofFloat(animationProperties.y, 0f)
            .setDuration(duration.toLong())
        yAnimator.apply {
            this.addUpdateListener {
                animationProperties.y = it.animatedValue as Float
            }
        }

        val cornerAnimator = ValueAnimator.ofFloat(animationProperties.corners, 0f)
            .setDuration(duration.toLong())
        cornerAnimator.apply {
            this.addUpdateListener {
                animationProperties.corners = it.animatedValue as Float
                listener.onAnimation(animationProperties)
            }
        }

        animatorsSet.playTogether(listOf(scaleAnimator, xAnimator, yAnimator, cornerAnimator))
        animatorsSet.start()
    }


}