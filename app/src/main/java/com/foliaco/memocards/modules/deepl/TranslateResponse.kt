package com.foliaco.memocards.modules.deepl


data class TranslateResponse(
    val translations:List<TranslateDataResponse>
)
data class TranslateDataResponse(
    val detected_source_language: String,
    val text: String
)