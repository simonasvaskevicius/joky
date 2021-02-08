package com.vaskevicius.android.joky.data.api

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vaskevicius.android.joky.BuildConfig
import com.vaskevicius.android.joky.data.model.Joke
import io.reactivex.Single

class ApiServiceImpl : ApiService {

    override fun getJoke(): Single<Joke> =
        Rx2AndroidNetworking.get(getJokeUrl()).build().getObjectSingle(Joke::class.java)

    override fun getSafeJoke(): Single<Joke> =
        Rx2AndroidNetworking.get(getSafeJokeUrl()).build().getObjectSingle(Joke::class.java)


    private fun getJokeUrl(): String = "${BuildConfig.BASE_URL}any"

    private fun getSafeJokeUrl(): String = "${BuildConfig.BASE_URL}any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit"
}