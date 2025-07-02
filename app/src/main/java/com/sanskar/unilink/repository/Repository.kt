package com.sanskar.unilink.repository

import android.util.Log
import android.util.Log.e
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

    suspend fun getUserProfile(): Resource<User> {
        return try {
            val email = auth.currentUser?.email ?: return Resource.Error(Exception("User not logged in"))
            val snapshot = userCol.document(email).get().await()
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error(Exception("User not found"))
            }
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

    suspend fun getItem(id: String, type: String): Resource<LostFoundItem> {
        return try {
            val collectionName = when (type.lowercase()) {
                "lost" -> "LostItems"
                "found" -> "FoundItems"
                else -> return Resource.Error(Exception("Invalid item type: $type"))
            }

            val doc = firestore.collection(collectionName).document(id).get().await()
            val item = doc.toObject(LostFoundItem::class.java)
            if (item != null) {
                Resource.Success(item)
            } else {
                Resource.Error(Exception("Item not found"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }



    suspend fun UpdateLostItems(id: String, lostFoundItem: LostFoundItem) {
        try {
            val itemRef = firestore.collection("LostItems").document(id)
            val item = itemRef.get().await().toObject(LostFoundItem::class.java)
            if (item != null) {
                itemRef.set(lostFoundItem).await()
                realtimeDb.child("LostLiveItems").child(id).setValue(
                    mapOf("title" to lostFoundItem.title, "type" to lostFoundItem.type, "status" to lostFoundItem.status)
                ).await()
            }
            } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun UpdateFoundItems(id: String, lostFoundItem: LostFoundItem) {
        try {
            val itemRef = firestore.collection("FoundItems").document(id)
            val item = itemRef.get().await().toObject(LostFoundItem::class.java)
            if (item != null) {
                itemRef.set(lostFoundItem).await()
                realtimeDb.child("FoundLiveItems").child(id).setValue(
                    mapOf("title" to lostFoundItem.title, "type" to lostFoundItem.type, "status" to lostFoundItem.status)
                ).await()
            }
            } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}
