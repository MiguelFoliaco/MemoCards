package com.foliaco.memocards.modules.deepl

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeeplServices {

    @POST("/v2/translate")
    suspend fun transalate(
        @Header("Authorization") authToken: String,
        @Header("Content-Type") headers: String = "application/json",
        @Body translateRequest: TranslateRequest
    ): Response<TranslateResponse>
}