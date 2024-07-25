package com.foliaco.memocards.modules.deepl

data class TranslateRequest(
    val text: MutableList<String>,
    val target_lang: String = "",
    val source_lang: String = ""
)