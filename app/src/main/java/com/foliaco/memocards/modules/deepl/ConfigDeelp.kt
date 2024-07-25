package com.foliaco.memocards.modules.deepl

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigDeelp {

    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-free.deepl.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiDeelpServices = retrofit.create(DeeplServices::class.java)
    }
}