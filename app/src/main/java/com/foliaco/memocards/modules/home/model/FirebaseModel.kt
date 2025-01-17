package com.foliaco.memocards.modules.home.model

import android.util.Log
import androidx.compose.ui.input.key.Key
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.typeOf

@Singleton
class FirebaseModel @Inject constructor() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    val lenguaje = mutableMapOf<String, Map<String, Any>>()
    fun getLenguajes() {
        db.collection(KeyCollections.LENGUAJE.collection).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lenguaje[document.id] = document.data
                }
            }
            .addOnFailureListener {
                println("Response ---> error $it")
            }
    }

    suspend fun getAllCardsWidgets(): Task<QuerySnapshot> {
        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection)
                .whereEqualTo("widget", true)
                .get()
        }
    }

    suspend fun getCardsByIdLenguajes(idLenguaje: String): Task<QuerySnapshot> {

        val lenguajeRef =
            db.collection(KeyCollections.LENGUAJE.collection)
                .document(if (idLenguaje.isEmpty()) "IaKxel8JylgZkr1nhrnU" else idLenguaje)

        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection)
                .whereEqualTo("lenguajeId", lenguajeRef)
                .whereEqualTo("made_in", "admin")

                //.whereArrayContainsAny("made_in", mutableListOf("admin", auth.currentUser!!.email))
                .get()
        }
        //  println("List of Firebase Model $listMemosSet")
    }

    suspend fun getCardsMeByIdLenguajes(idLenguaje: String): Task<QuerySnapshot> {

        val lenguajeRef =
            db.collection(KeyCollections.LENGUAJE.collection)
                .document(if (idLenguaje.isEmpty()) "IaKxel8JylgZkr1nhrnU" else idLenguaje)

        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection)
                .whereEqualTo("lenguajeId", lenguajeRef)
                .whereEqualTo("made_in", auth.currentUser!!.email!!)

                //.whereArrayContainsAny("made_in", mutableListOf("admin", auth.currentUser!!.email))
                .get()
        }
        //  println("List of Firebase Model $listMemosSet")
    }


    suspend fun enableOrDisableWidget(card: Memos, idCard: String) {
        val cardUpdate = hashMapOf<String, Any?>()
//        cardUpdate.put("lenguajeId", card.lenguajeId)
//        cardUpdate.put("value", card.value)
//        cardUpdate.put("key", card.key)
        if (card.widget == true) {
            cardUpdate["widget"] = false
        } else {
            cardUpdate["widget"] = true
        }
//        cardUpdate.put("reading_on", card.reading_on)
//        cardUpdate.put("reading_kun", card.reading_kun)
//        cardUpdate.put("reading", card.reading)
//        cardUpdate.put("nivel", card.nivel)

        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection).document(idCard)
                .update(cardUpdate)
        }
    }

    suspend fun saveMemo(memo: Memos) {
        val memoCreate = hashMapOf<String, Any?>()
        val lenguajeReference =
            db.collection(KeyCollections.LENGUAJE.collection).document(memo.lenguajeId!!)
        memoCreate.put("key", memo.key)
        memoCreate.put("value", memo.value)
        memoCreate.put("reading", memo.reading.orEmpty())
        memoCreate.put("reading_on", memo.reading_on.orEmpty())
        memoCreate.put("reading_kun", memo.reading_kun.orEmpty())
        memoCreate.put("widget", memo.widget!!.or(false))
        memoCreate.put("lenguajeId", lenguajeReference)
        memoCreate.put("made_in", auth.currentUser!!.email)
        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection).add(memoCreate)
        }
    }

    suspend fun updateMemo(memo: Memos) {
        val memoCreate = hashMapOf<String, Any?>()
        memoCreate.put("key", memo.key)
        memoCreate.put("value", memo.value)
        memoCreate.put("reading", memo.reading.orEmpty())
        memoCreate.put("reading_on", memo.reading_on.orEmpty())
        memoCreate.put("reading_kun", memo.reading_kun.orEmpty())
        memoCreate.put("widget", memo.widget!!.or(false))
        memoCreate.put("made_in", auth.currentUser!!.email)
        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection).document(memo.id).update(memoCreate)
        }
    }

    suspend fun deleteMemo(id: String) {
        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection).document(id).delete()
        }
    }

    fun getLenguaje(id: String): String {
        return db.collection(KeyCollections.LENGUAJE.collection).document(id).id
    }
}