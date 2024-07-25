package com.foliaco.memocards.modules.deepl

import com.foliaco.memocards.BuildConfig
import com.foliaco.memocards.modules.deepl.ConfigDeelp.Companion.apiDeelpServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslateUseCase @Inject constructor() {
    private val scope = CoroutineScope(Dispatchers.IO)
    var listResponseTranslate = mutableListOf<TranslateDataResponse>()
    fun translate(
        text: MutableList<String>,
        sourceTag: String,
        targetLang: String
    ) {
        val translateRequest = TranslateRequest(
            text = text,
            source_lang = sourceTag,
            target_lang = targetLang
        )
        scope.launch {
            val response =
                apiDeelpServices.transalate(
                    authToken = "DeepL-Auth-Key f7ecb189-6ba9-49ef-9d91-502462ca182f:fx",
                    translateRequest = translateRequest
                )
            val data = response.body()
            if (data != null) {
                listResponseTranslate = data.translations.toMutableList()
            }
        }

    }
}