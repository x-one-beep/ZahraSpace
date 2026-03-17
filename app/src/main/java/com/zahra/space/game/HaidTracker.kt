package com.zahra.space.game

import java.util.*
import kotlin.math.*

data class CycleData(
    val startDate: Date,
    val endDate: Date,
    val cycleLength: Int,
    val periodLength: Int,
    val symptoms: List<String> = emptyList(),
    val notes: String = ""
)

data class Prediction(
    val nextPeriodDate: Date,
    val ovulationDate: Date,
    val fertileWindowStart: Date,
    val fertileWindowEnd: Date,
    val safePeriodStart: Date,
    val safePeriodEnd: Date,
    val confidenceLevel: Double // 0-1 based on data history
)

class HaidTracker {
    private val cycleHistory = mutableListOf<CycleData>()
    private val averageCycleLength = 28
    private val averagePeriodLength = 5
    
    // Add new cycle data
    fun addCycle(startDate: Date, endDate: Date, symptoms: List<String> = emptyList(), notes: String = "") {
        val cycleLength = daysBetween(startDate, endDate)
        val periodLength = daysBetween(startDate, endDate)
        
        val cycle = CycleData(
            startDate = startDate,
            endDate = endDate,
            cycleLength = cycleLength,
            periodLength = periodLength,
            symptoms = symptoms,
            notes = notes
        )
        
        cycleHistory.add(cycle)
    }
    
    // Calculate average cycle length
    fun getAverageCycleLength(): Int {
        if (cycleHistory.isEmpty()) return averageCycleLength
        return cycleHistory.map { it.cycleLength }.average().roundToInt()
    }
    
    // Calculate average period length
    fun getAveragePeriodLength(): Int {
        if (cycleHistory.isEmpty()) return averagePeriodLength
        return cycleHistory.map { it.periodLength }.average().roundToInt()
    }
    
    // Predict next cycle
    fun predictNextCycle(): Prediction? {
        if (cycleHistory.isEmpty()) return null
        
        val lastCycle = cycleHistory.last()
        val avgCycle = getAverageCycleLength()
        val avgPeriod = getAveragePeriodLength()
        
        val calendar = Calendar.getInstance()
        calendar.time = lastCycle.startDate
        calendar.add(Calendar.DAY_OF_MONTH, avgCycle)
        val nextPeriodDate = calendar.time
        
        // Ovulation occurs 14 days before next period
        calendar.time = nextPeriodDate
        calendar.add(Calendar.DAY_OF_MONTH, -14)
        val ovulationDate = calendar.time
        
        // Fertile window (5 days before ovulation + day of ovulation)
        calendar.time = ovulationDate
        calendar.add(Calendar.DAY_OF_MONTH, -5)
        val fertileStart = calendar.time
        
        calendar.time = ovulationDate
        val fertileEnd = ovulationDate
        
        // Safe period (after period ends, before fertile window)
        calendar.time = lastCycle.endDate
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val safeStart = calendar.time
        
        calendar.time = fertileStart
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val safeEnd = calendar.time
        
        // Calculate confidence based on data consistency
        val confidence = if (cycleHistory.size >= 6) {
            val deviations = cycleHistory.map { abs(it.cycleLength - avgCycle) }
            val avgDeviation = deviations.average()
            1.0 - (avgDeviation / avgCycle).coerceIn(0.0, 1.0)
        } else {
            cycleHistory.size * 0.15 // Less data = less confidence
        }
        
        return Prediction(
            nextPeriodDate = nextPeriodDate,
            ovulationDate = ovulationDate,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            safePeriodStart = safeStart,
            safePeriodEnd = safeEnd,
            confidenceLevel = confidence.coerceIn(0.0, 1.0)
        )
    }
    
    // Check if currently in period
    fun isInPeriod(date: Date = Date()): Boolean {
        if (cycleHistory.isEmpty()) return false
        
        val lastCycle = cycleHistory.last()
        return date in lastCycle.startDate..lastCycle.endDate
    }
    
    // Get days until next period
    fun daysUntilNextPeriod(): Int? {
        val prediction = predictNextCycle() ?: return null
        return daysBetween(Date(), prediction.nextPeriodDate)
    }
    
    // Calculate days between two dates
    private fun daysBetween(start: Date, end: Date): Int {
        val diff = end.time - start.time
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
    
    // Get cycle regularity status
    fun getRegularityStatus(): String {
        if (cycleHistory.size < 3) return "Masih sedikit data"
        
        val cycles = cycleHistory.map { it.cycleLength }
        val avg = cycles.average()
        val variance = cycles.map { (it - avg).pow(2) }.average()
        val stdDev = sqrt(variance)
        
        return when {
            stdDev < 2 -> "Sangat teratur"
            stdDev < 4 -> "Teratur"
            stdDev < 7 -> "Cukup teratur"
            else -> "Kurang teratur"
        }
    }
    
    // Get symptoms frequency
    fun getSymptomsFrequency(): Map<String, Int> {
        val symptomMap = mutableMapOf<String, Int>()
        cycleHistory.forEach { cycle ->
            cycle.symptoms.forEach { symptom ->
                symptomMap[symptom] = symptomMap.getOrDefault(symptom, 0) + 1
            }
        }
        return symptomMap
    }
}
