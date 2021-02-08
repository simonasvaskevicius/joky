package com.vaskevicius.android.joky.ui.joke

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.vaskevicius.android.joky.R
import com.vaskevicius.android.joky.data.api.ApiHelper
import com.vaskevicius.android.joky.data.api.ApiServiceImpl
import com.vaskevicius.android.joky.data.model.Joke
import com.vaskevicius.android.joky.data.preferences.SharedPreferencesUtil
import com.vaskevicius.android.joky.ui.base.ViewModelFactory
import com.vaskevicius.android.joky.ui.onboarding.OnBoardingActivity
import com.vaskevicius.android.joky.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class JokeActivity : AppCompatActivity() {

    private lateinit var jokeViewModel: JokeViewModel
    private var currentJokeType: String? = null
    private var prefs: SharedPreferencesUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreferencesUtil(this)
        //if user opens app for the first time, app redirects user to onBoarding
        if (prefs!!.isFirstTimeLaunch) startActivity(Intent(this, OnBoardingActivity::class.java))
        //after onBoarding user gets redirected back to JokeActivity
        setContentView(R.layout.activity_main)

        if(!prefs!!.isSafeModeEnabled()) safemode.setColorFilter(ContextCompat.getColor(this, R.color.white))
        setupFirstLoginJoke()
        setupSafeMode()
    }

    //safe mode is a mode, that filters out all nsfw, religious, political, racist, sexist, explicit jokes
    private fun setupSafeMode() {
        safemode.setOnClickListener {
            if (prefs!!.isSafeModeEnabled()) {
                //disable safeMode
                prefs!!.enableSafeMode(false)
                safemode.setColorFilter(ContextCompat.getColor(this, R.color.white))
                showSnackBar(getString(R.string.safe_mode_disabled))

            } else {
                //enable safeMode
                prefs!!.enableSafeMode(true)
                safemode.setColorFilter(ContextCompat.getColor(this, R.color.primaryDark))
                showSnackBar(getString(R.string.safe_mode_enabled))
            }
        }
    }

    private fun setupFirstLoginJoke() {
        if (prefs!!.isFirstTimeLaunch) {
            //user opened app for the first time
            tutorialJoke.visibility = View.VISIBLE
            singleJoke.visibility = View.GONE
            setup.visibility = View.GONE
            delivery.visibility = View.GONE
            container.setOnClickListener {
                //extra textView for tutorial text is added because i'm lazy TODO:modify tutorial
                tutorialJoke.visibility = View.GONE
                initJoke()
            }
        } else initJoke()
    }

    private fun initJoke() {
        setupViewModel()
        getJoke(false)
        setupNextJoke()
    }

    /*there is two types of joke result:
       1. Single, one line joke. If so 'setupNextJoke()' just goes to the next joke on click.
       2. Two line joke, with setup and delivery. In this case on first click it makes delivery visible, on second click it goes to the next joke.
       both are being processed here
    */
    private fun setupNextJoke() {
        container.setOnClickListener {
            when (currentJokeType) {
                Joke.TYPE_SINGLE -> {
                    nextJoke()
                }

                Joke.TYPE_TWO_PART -> {
                    if (delivery.visibility == View.GONE) {
                        delivery.visibility = View.VISIBLE
                    } else {
                        nextJoke()
                        delivery.visibility = View.GONE
                    }
                }
            }
        }
    }

    //reloads, by doing that gets new joke
    private fun nextJoke() {
        jokeViewModel.refresh(prefs!!.isSafeModeEnabled())
        getJoke(true)
    }

    //gets joke form ViewModel
    private fun getJoke(nextJoke: Boolean) {
        jokeViewModel.getJoke().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (!nextJoke) progressBar.visibility = View.GONE
                    else jokeLayout.visibility = View.VISIBLE
                    safemode.visibility = View.VISIBLE
                    it.data?.let { joke -> setJoke(joke) }
                }

                Status.LOADING -> {
                    //between jokes it doesn't show progress bar. Progress bar is only shown when app is launched
                    if (!nextJoke) progressBar.visibility = View.VISIBLE
                    else {
                        progressBar.visibility = View.GONE
                        jokeLayout.visibility = View.GONE
                    }
                }

                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    showSnackBar(it.message!!)
                }
            }
        })
    }

    //snackBar for displaying messages
    private fun showSnackBar(text: String) {
        val snackBar = Snackbar.make(container, text, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(getColor(R.color.primaryDark))
        snackBar.show()
    }

    //sets joke depending on what type is it( single, two part)
    private fun setJoke(joke: Joke?) {
        when (joke?.type) {
            Joke.TYPE_SINGLE -> {
                currentJokeType = Joke.TYPE_SINGLE
                jokeLayout.visibility = View.VISIBLE
                setup.visibility = View.GONE
                delivery.visibility = View.GONE
                singleJoke.text = joke?.singleJoke
                singleJoke.visibility = View.VISIBLE
            }

            Joke.TYPE_TWO_PART -> {
                currentJokeType = Joke.TYPE_TWO_PART
                jokeLayout.visibility = View.VISIBLE
                singleJoke.visibility = View.GONE
                setup.text = joke?.setup
                setup.visibility = View.VISIBLE
                delivery.text = joke?.delivery
            }
        }
    }

    private fun setupViewModel() {
        jokeViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(JokeViewModel::class.java)
    }
}