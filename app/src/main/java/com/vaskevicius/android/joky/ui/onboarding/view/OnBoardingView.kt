package com.vaskevicius.android.joky.ui.onboarding.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.vaskevicius.android.joky.R
import com.vaskevicius.android.joky.data.preferences.SharedPreferencesUtil
import com.vaskevicius.android.joky.ui.onboarding.OnBoardingPage
import com.vaskevicius.android.joky.ui.onboarding.OnBoardingPagerAdapter
import com.vaskevicius.android.joky.ui.onboarding.transitions.setParallaxTransformation
import kotlinx.android.synthetic.main.view_onboarding.view.*

class OnBoardingView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
    private val prefs: SharedPreferencesUtil
    private var listener: FinishListener? =null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_onboarding, this, true)
        setupSlider(view)
        setButtonsOnClick()
        prefs = SharedPreferencesUtil(view.context)
    }

    fun setOnFinishListener(listener: FinishListener) {
        this.listener = listener
    }

    private fun setupSlider(view: View) {
        with(slider) {
            adapter = OnBoardingPagerAdapter()
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }

            addSlideChangeListener()

            val wormDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.page_indicator)
            wormDotsIndicator.setViewPager2(this)
        }
    }


    private fun addSlideChangeListener() {
        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    private fun setButtonsOnClick() {
        nextBtn.setOnClickListener { navigateToNextSlide() }
        skipBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
            listener?.onGetStartedPressed()
        }
        startBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
            listener?.onGetStartedPressed()
        }
    }

    private fun setFirstTimeLaunchToFalse() {
        prefs!!.isFirstTimeLaunch = false
    }

    private fun navigateToNextSlide() {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        slider?.setCurrentItem(nextSlidePos, true)
    }

    //for OnBoardingActivity, so it would know, "get started" or "skip" button is pressed
    interface FinishListener {
        fun onGetStartedPressed()
    }
}