package com.zahra.space.utils

import java.util.*

object HijriCalendar {
    
    private val hijriMonths = arrayOf(
        "Muharram", "Safar", "Rabi'ul Awwal", "Rabi'ul Akhir",
        "Jumadil Ula", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadhan", "Syawwal", "Dzulqa'dah", "Dzulhijjah"
    )
    
    fun toHijri(gregorianDate: Date): HijriDate {
        val calendar = Calendar.getInstance()
        calendar.time = gregorianDate
        
        // Simplified conversion (in production, use accurate algorithms)
        val year = calendar.get(Calendar.YEAR) - 622
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        return HijriDate(year, month + 1, day, hijriMonths[month])
    }
    
    fun getHijriMonthName(month: Int): String {
        return hijriMonths.getOrElse(month - 1) { "" }
    }
}

data class HijriDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val monthName: String
) {
    override fun toString(): String {
        return "$day $monthName $year H"
    }
}
