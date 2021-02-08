package com.vaskevicius.android.joky.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vaskevicius.android.joky.R
import com.vaskevicius.android.joky.ui.joke.JokeActivity
import com.vaskevicius.android.joky.ui.onboarding.view.OnBoardingView
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        container.setOnFinishListener(object: OnBoardingView.FinishListener{
            override fun onGetStartedPressed() {
                finish()
            }
        })
    }


}