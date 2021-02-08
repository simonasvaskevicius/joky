package com.vaskevicius.android.joky.data.api

class ApiHelper(private val apiService: ApiService) {

    fun getJoke() = apiService.getJoke()

    fun getSafeJoke() = apiService.getSafeJoke()

}