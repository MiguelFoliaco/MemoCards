package com.foliaco.memocards.modules.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foliaco.memocards.modules.home.model.FirebaseModel
import com.foliaco.memocards.modules.home.model.Memos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val firebaseModel: FirebaseModel
) : ViewModel() {

    val lenguajeSelect = MutableLiveData<String>("Japones/日本語")
    var memos = MutableLiveData<MutableList<Memos>>(mutableListOf())

    fun setLenguaje(name: String) {
        lenguajeSelect.postValue(name)
    }

    fun getLenguajes(): MutableMap<String, Map<String, Any>> {

        if (firebaseModel.lenguaje.isEmpty()) {
            firebaseModel.getLenguajes()
            return firebaseModel.lenguaje
        }
        return firebaseModel.lenguaje
    }

    fun getCardByIdLenguaje() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val memosValues = mutableListOf<Memos>()
            val result = firebaseModel.getCardsByIdLenguajes().await()
            for (doc in result.documents) {
                var item = Memos(
                    id = doc.id,
                    lenguajeId = doc["lenguajeId"].toString(),
                    key = doc["key"].toString(),
                    nivel = doc["nivel"].toString(),
                    reading_on = doc["reading_on"].toString(),
                    reading_kun = doc["reading_kun"].toString(),
                    value = doc["value"].toString(),
                    reading = doc["reading"].toString(),
                    widget = (doc["widget"] ?: false) as Boolean
                )
                memosValues.add(item)
            }
            memos.postValue(memosValues)
        }
            .start()
    }
}