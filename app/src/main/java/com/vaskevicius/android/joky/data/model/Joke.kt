package com.vaskevicius.android.joky.data.model

import com.google.gson.annotations.SerializedName

data class Joke(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("safe")
    val safe: Boolean = true,

    @SerializedName("joke")
    val singleJoke: String? = null,

    @SerializedName("setup")
    val setup: String? = null,

    @SerializedName("delivery")
    val delivery: String? = null
) {
    companion object {
        const val TYPE_SINGLE = "single"
        const val TYPE_TWO_PART = "twopart"
    }
}