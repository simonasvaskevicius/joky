package com.vaskevicius.android.joky.data.api

import com.vaskevicius.android.joky.data.model.Joke
import io.reactivex.Single

interface ApiService {

    fun getJoke(): Single<Joke>

    fun getSafeJoke(): Single<Joke>


}