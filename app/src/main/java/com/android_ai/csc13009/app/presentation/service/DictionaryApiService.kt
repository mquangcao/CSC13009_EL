package com.android_ai.csc13009.app.presentation.service

import com.android_ai.csc13009.app.data.remote.model.WordPhoneticResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    fun getWordPhonetics(@Path("word") word: String): Call<List<WordPhoneticResponse>>
}