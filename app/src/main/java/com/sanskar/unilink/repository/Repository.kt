package com.sanskar.unilink.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.sanskar.unilink.Resource
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.models.User
import kotlinx.coroutines.tasks.await

class Repository {

    private val firestore = FirebaseFirestore.getInstance()
    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val userCol = firestore.collection("User")

    suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signUp(user: User, password: String): Resource<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(user.email, password).await()
            userCol.document(user.email).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.d("SignUp", "signUp: ${e.message}")
            Resource.Error(e)
        }
    }

    suspend fun signOut(): Resource<Unit> {
        return try {
            auth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun editUserProfile(user: User): Resource<Unit> {
        return try {
            userCol.document(user.email).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun lostItem(item: LostFoundItem): Resource<Unit> {
        return try {
            firestore.collection("LostItems").document(item.id).set(item).await()
            realtimeDb.child("LostLiveItems").child(item.id).setValue(
                mapOf("title" to item.title, "type" to item.type, "status" to item.status)
            ).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun foundItem(item: LostFoundItem): Resource<Unit> {
        return try {
            firestore.collection("FoundItems").document(item.id).set(item).await()
            realtimeDb.child("FoundLiveItems").child(item.id).setValue(
                mapOf("title" to item.title, "type" to item.type, "status" to item.status)
            ).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getLostItems(): Resource<List<LostFoundItem>> {
        return try {
            val snapshot = firestore.collection("LostItems").get().await()
            val items = snapshot.documents.mapNotNull { it.toObject(LostFoundItem::class.java) }
            Resource.Success(items)
            } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getFoundItems(): Resource<List<LostFoundItem>> {
        return try {
            val snapshot = firestore.collection("FoundItems").get().await()
            val items = snapshot.documents.mapNotNull { it.toObject(LostFoundItem::class.java) }
            Resource.Success(items)
            } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
