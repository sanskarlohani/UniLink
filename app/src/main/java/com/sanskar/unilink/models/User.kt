package com.sanskar.unilink.models

data class User(
    val name: String = "",
    val email: String = "",
    val sic: String = "",
    val year: String = "",
    val semester: String = "",
    val college: String = "",
    val uid: String = System.currentTimeMillis().toString()
)
