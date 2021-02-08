package com.vaskevicius.android.joky.ui.joke

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vaskevicius.android.joky.data.model.Joke
import com.vaskevicius.android.joky.data.repository.JokeRepository
import com.vaskevicius.android.joky.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JokeViewModel(private val jokeRepository: JokeRepository) :
    ViewModel() {
    private val joke = MutableLiveData<Resource<Joke>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchSafeJoke()
    }

    //fetches not censored joke from db
    private fun fetchJoke() {
        joke.postValue(Resource.loading(null))
        compositeDisposable.add(
            jokeRepository.getJoke().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ joke ->
                    this.joke.postValue(Resource.success(joke))
                }, {
                    joke.postValue(Resource.error("Something went wrong", null))
                })
        )
    }

    //fetches censored joke from db
    private fun fetchSafeJoke() {
        joke.postValue(Resource.loading(null))
        compositeDisposable.add(
            jokeRepository.getSafeJoke().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ joke ->
                    this.joke.postValue(Resource.success(joke))
                }, {
                    joke.postValue(Resource.error("Something went wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun refresh(safeMode: Boolean) {
        if (safeMode) fetchSafeJoke()
        else fetchJoke()
    }

    fun getJoke(): LiveData<Resource<Joke>> = joke
}