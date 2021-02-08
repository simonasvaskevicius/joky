package com.vaskevicius.android.joky.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaskevicius.android.joky.data.api.ApiHelper
import com.vaskevicius.android.joky.data.repository.JokeRepository
import com.vaskevicius.android.joky.ui.joke.JokeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JokeViewModel::class.java)) {
            return JokeViewModel(JokeRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}