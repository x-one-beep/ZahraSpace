package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_ayat")
data class QuranAyat(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahId: Int,
    val surahName: String,
    val surahNameLatin: String,
    val juz: Int,
    val ayatNumber: Int,
    val arabicText: String,
    val latinText: String,
    val translationId: String,
    val tafsir: String = "",
    val page: Int,
    val isMakkiyah: Boolean = true,
    val audioUrl: String = ""
)
