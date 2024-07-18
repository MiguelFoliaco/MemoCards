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
    val lenguajeIdSelect = MutableLiveData<String>("")
    var memos = MutableLiveData<MutableList<Memos>>(mutableListOf())
    var isLoadingMemos = MutableLiveData<Boolean>(true)

    fun setLenguaje(name: String, id: String) {
        lenguajeSelect.postValue(name)
        lenguajeIdSelect.postValue(id)
    }

    fun getLenguajes(): MutableMap<String, Map<String, Any>> {

        if (firebaseModel.lenguaje.isEmpty()) {
            firebaseModel.getLenguajes()
            return firebaseModel.lenguaje
        }
        return firebaseModel.lenguaje
    }

    fun getCardByIdLenguaje(id: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isLoadingMemos.postValue(true)
            val memosValues = mutableListOf<Memos>()
            val result = firebaseModel.getCardsByIdLenguajes(id).await()
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
            isLoadingMemos.postValue(false)

        }.start()
    }

    fun enableOrDisableWidget(memo: Memos, idMemo: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isLoadingMemos.postValue(true)
            firebaseModel.enableOrDisableWidget(
                card = memo,
                idCard = idMemo
            )
            val id = memo.lenguajeId
            if (id != null) {
                getCardByIdLenguaje(id)
            }
            isLoadingMemos.postValue(false)
        }.start()
    }
}
