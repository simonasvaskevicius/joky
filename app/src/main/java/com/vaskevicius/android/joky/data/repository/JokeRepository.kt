package com.vaskevicius.android.joky.data.repository

import com.vaskevicius.android.joky.data.api.ApiHelper
import com.vaskevicius.android.joky.data.model.Joke
import io.reactivex.Single

class JokeRepository(private val apiHelper: ApiHelper) {

    fun getJoke(): Single<Joke> = apiHelper.getJoke()

    fun getSafeJoke(): Single<Joke> = apiHelper.getSafeJoke()
}