package com.sanskar.unilink.models

import java.util.UUID

data class LostFoundItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val type: String = "", // "lost" or "found"
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "active" // or "claimed"

)
