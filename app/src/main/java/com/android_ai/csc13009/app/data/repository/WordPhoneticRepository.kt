package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.model.WordPhoneticResponse
import com.android_ai.csc13009.app.domain.models.WordPhonetic
import com.android_ai.csc13009.app.domain.repository.IWordPhoneticRepository
import com.android_ai.csc13009.app.presentation.service.DictionaryApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordPhoneticRepository(private val apiService: DictionaryApiService) :
    IWordPhoneticRepository {

    override fun getWordPhonetics(
        word: String,
        onSuccess: (List<WordPhonetic>) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.getWordPhonetics(word).enqueue(object : Callback<List<WordPhoneticResponse>> {
            override fun onResponse(
                call: Call<List<WordPhoneticResponse>>,
                response: Response<List<WordPhoneticResponse>>
            ) {
                if (response.isSuccessful) {
                    val phonetics = response.body()?.firstOrNull()?.phonetics?.map {
                        WordPhonetic(audio = it.audio)
                    } ?: emptyList()
                    onSuccess(phonetics)
                } else {
                    onError("Failed to fetch pronunciation")
                }
            }

            override fun onFailure(call: Call<List<WordPhoneticResponse>>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }
}