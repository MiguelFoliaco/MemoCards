package com.foliaco.memocards.modules.home.model

data class Memos(
    val id: String,
    var key: String? = null,
    var lenguajeId: String? = null,
    var nivel: String? = null,
    var reading: String? = null,
    var reading_on: String? = null,
    var reading_kun: String? = null,
    var value: String? = null,
    var widget: Boolean?
)