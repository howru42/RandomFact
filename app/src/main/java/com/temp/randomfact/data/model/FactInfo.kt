package com.temp.randomfact.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Facts")
data class FactInfo(
    @PrimaryKey
    val id: String,
    val text: String,
    val source: String,
    val source_url: String,
    val language: String,
    val permalink: String,
    var publishDate: Date? = null,
    var isRead: Boolean = false
)
