package com.sanskar.unilink

import android.content.Context
import android.content.SharedPreferences
import com.sanskar.unilink.models.User
import androidx.core.content.edit
import com.google.gson.Gson


object SharedPrefManager {
    private const val PREF_NAME = "UniLinkPrefs"
    private const val USER_KEY = "cached_user"
    private lateinit var preferences: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(user: User) {
        preferences.edit { putString(USER_KEY, gson.toJson(user)) }
    }

    fun getUser(): User? {
        return preferences.getString(USER_KEY, null)?.let {
            gson.fromJson(it, User::class.java)
        }
    }

    fun clearUser() {
        preferences.edit { remove(USER_KEY) }
    }
}