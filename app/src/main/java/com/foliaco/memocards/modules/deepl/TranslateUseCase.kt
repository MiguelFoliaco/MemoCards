package com.foliaco.memocards.modules.deepl

import com.foliaco.memocards.BuildConfig
import com.foliaco.memocards.modules.deepl.ConfigDeelp.Companion.apiDeelpServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject
import javax.inject.Singleton

class TranslateUseCase @Inject constructor() {
    suspend fun translate(
        text: MutableList<String>, sourceTag: String, targetLang: String
    ): MutableList<TranslateDataResponse> {
        val translateRequest = TranslateRequest(
            text = text, source_lang = sourceTag, target_lang = targetLang
        )
        return withContext<MutableList<TranslateDataResponse>>(Dispatchers.IO) {
            val response = apiDeelpServices.transalate(
                authToken = "DeepL-Auth-Key f7ecb189-6ba9-49ef-9d91-502462ca182f:fx",
                translateRequest = translateRequest
            )
            val data = response.body()
            if (data != null) {
                println("Data Paso 2 ${data.translations}")
                return@withContext data.translations.toMutableList()
            }
            return@withContext mutableListOf()
        }
    }
}