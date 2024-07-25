package com.foliaco.memocards.modules.home.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foliaco.memocards.modules.deepl.TranslateDataResponse
import com.foliaco.memocards.modules.deepl.TranslateResponse
import com.foliaco.memocards.modules.deepl.TranslateUseCase
import com.foliaco.memocards.modules.economia.model.CoinManager
import com.foliaco.memocards.modules.home.model.FirebaseModel
import com.foliaco.memocards.modules.home.model.Memos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val firebaseModel: FirebaseModel,
    private val coinManager: CoinManager,
    private val translateServices: TranslateUseCase
) : ViewModel() {

    val lenguajeSelect = MutableLiveData<String>("Japones/日本語")
    val lenguajeIdSelect = MutableLiveData<String>("")
    var memos = MutableLiveData<MutableList<Memos>>(mutableListOf())
    var isLoadingMemos = MutableLiveData<Boolean>(true)
    val isLoadinCreateMemos = MutableLiveData(false)
    val isLoadinDeleteMemos = MutableLiveData(false)
    val listTraductions = MutableLiveData<MutableList<TranslateDataResponse>>()
    val isSuccessFullOrError = MutableLiveData<String>("")
    val cointCount = MutableLiveData<Long>(0)
    val memoCreate = MutableLiveData(
        Memos(
            id = "", widget = false
        )
    )

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
            val result2 = firebaseModel.getCardsMeByIdLenguajes(id).await()
            val docs = result.documents.union(result2.documents)
            for (doc in docs) {
                var item = Memos(
                    id = doc.id,
                    lenguajeId = doc["lenguajeId"].toString(),
                    key = doc["key"].toString(),
                    nivel = doc["nivel"].toString(),
                    reading_on = doc["reading_on"].toString(),
                    reading_kun = doc["reading_kun"].toString(),
                    value = doc["value"].toString(),
                    reading = doc["reading"].toString(),
                    made_in = doc["made_in"].toString(),
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
                card = memo, idCard = idMemo
            )
            val id = memo.lenguajeId
            if (id != null) {
                getCardByIdLenguaje(id)
            }
            isLoadingMemos.postValue(false)
        }.start()
    }

    fun saveMemoDb() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isLoadinCreateMemos.postValue(true)
            try {
                firebaseModel.saveMemo(memoCreate.value!!)
                isSuccessFullOrError.postValue("Todo bien")
            } catch (er: Exception) {
                Log.i("Error firebase Model", "${er.message}")
                isSuccessFullOrError.postValue("Todo mal")
            }
            isLoadinCreateMemos.postValue(false)
        }
    }

    fun updateMemoDb() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isLoadinCreateMemos.postValue(true)
            try {
                firebaseModel.updateMemo(memoCreate.value!!)
                isSuccessFullOrError.postValue("Todo bien")
            } catch (er: Exception) {
                Log.i("Error firebase Model", "${er.message}")
                isSuccessFullOrError.postValue("Todo mal")
            }
            isLoadinCreateMemos.postValue(false)
        }
    }

    fun deleteMemo(id: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            isLoadinDeleteMemos.postValue(true)
            try {
                firebaseModel.deleteMemo(id)
                val m = memos.value
                if (m != null) {
                    var index = -1
                    m.forEachIndexed(action = { i, item ->
                        if (item.id == id) {
                            index = i
                        }
                    })
                    if (index != -1) {
                        m.removeAt(index)
                        println("Index data ${index} ${m}")
                        memos.postValue(m ?: mutableListOf())
                        isLoadingMemos.postValue(true)
                        delay(500)
                        isLoadingMemos.postValue(false)
                    }
                }
            } catch (err: Exception) {
                Log.i("Error firebase Model", "${err.message}")
                isSuccessFullOrError.postValue("Todo mal")
            }
            isLoadinDeleteMemos.postValue(false)
        }
    }

    fun getCoinByUser() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val result = coinManager.getCoin().await()
            if (result.documents.size > 0) {
                val coin = result.documents[0]
                cointCount.postValue((coin["count"] as Long?)?.or(0))
            }
        }
    }

    fun translate() {

        if (memoCreate.value?.key != null && memoCreate.value?.lenguajeId != null) {
            val lenguajes = getLenguajes()
            val key = memoCreate.value?.lenguajeId!!.toString()
            if (lenguajes != null && key.length > 0) {
                val text = mutableListOf<String>()
                text.add("${memoCreate.value!!.key}")
                val code = lenguajes!![key]!!["code"] as String?
                if (code != null) {
                    translateServices.translate(
                        text = text,
                        sourceTag = code,
                        targetLang = "ES"
                    )
                    println("Traducido")
                    listTraductions.postValue(translateServices.listResponseTranslate)
                }
            }
        }
    }
}
