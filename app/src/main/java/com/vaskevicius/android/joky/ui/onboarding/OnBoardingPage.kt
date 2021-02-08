package com.vaskevicius.android.joky.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vaskevicius.android.joky.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val imgResource: Int
) {

    ONE(R.string.onboarding_slide1_title, R.string.onboarding_slide1_subtitle,R.string.onboarding_slide1_desc, R.drawable.ic_get_started),
    TWO(R.string.onboarding_slide2_title, R.string.onboarding_slide2_subtitle,R.string.onboarding_slide2_desc, R.drawable.ic_notification),
    THREE(R.string.onboarding_slide3_title, R.string.onboarding_slide3_subtitle,R.string.onboarding_slide3_desc, R.drawable.ic_security)

}