package com.foliaco.memocards.modules.economia.model

import com.foliaco.memocards.modules.home.model.KeyCollections
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinManager @Inject constructor(){
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser!!

    suspend fun getCoin(): Task<QuerySnapshot> {
        return withContext(Dispatchers.IO) {
            db.collection(KeyCollections.ECONOMIA.collection)
                .whereEqualTo("UUID", user.uid)
                .get()
        }
    }
}