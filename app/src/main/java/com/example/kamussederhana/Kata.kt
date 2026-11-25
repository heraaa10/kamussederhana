package com.example.kamussederhana

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kata")
data class Kata(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val indonesia: String,
    val inggris: String
)