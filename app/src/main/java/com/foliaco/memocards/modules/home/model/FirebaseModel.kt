package com.foliaco.memocards.modules.home.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseModel @Inject constructor() {

    private val db = Firebase.firestore
    val lenguaje = mutableMapOf<String, Map<String, Any>>()
    val listMemosSet = mutableSetOf<Memos>()
    fun getLenguajes() {
        db.collection(KeyCollections.LENGUAJE.collection).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    print("Data --> ${document.data}")
                    lenguaje[document.id] = document.data
                }
            }
            .addOnFailureListener {
                println("Response ---> error $it")
            }
    }

    suspend fun getCardsByIdLenguajes(): Task<QuerySnapshot> {

        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.MEMOS.collection)
                .get()
        }
        //  println("List of Firebase Model $listMemosSet")
    }
}