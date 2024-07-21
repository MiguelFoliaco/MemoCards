package com.foliaco.memocards.modules.home.model

data class Memos(
    val id: String,
    var key: String? = "",
    var lenguajeId: String? = "",
    var nivel: String? = "",
    var reading: String? = "",
    var reading_on: String? = "",
    var reading_kun: String? = "",
    var value: String? = "",
    var widget: Boolean?,
    var made_in: String? = ""
)