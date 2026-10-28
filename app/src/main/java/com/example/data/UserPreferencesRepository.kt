package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SalaryConfig(
    val monthlyGrossSalary: Double = 3800.0,
    val weeklyHours: Double = 40.0,
    val hapticsEnabled: Boolean = true,
    val soundEnabled: Boolean = true
) {
    val monthlyHours: Double get() = (weeklyHours * 4.3333).coerceAtLeast(1.0)
    val hourlyRate: Double get() = monthlyGrossSalary / monthlyHours
    val minuteRate: Double get() = hourlyRate / 60.0
    val secondRate: Double get() = hourlyRate / 3600.0
}

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("robinpay_prefs", Context.MODE_PRIVATE)

    private val _salaryConfig = MutableStateFlow(loadConfig())
    val salaryConfig: StateFlow<SalaryConfig> = _salaryConfig.asStateFlow()

    private fun loadConfig(): SalaryConfig {
        val gross = prefs.getFloat("monthly_gross", 3800f).toDouble()
        val hours = prefs.getFloat("weekly_hours", 40f).toDouble()
        val haptics = prefs.getBoolean("haptics_enabled", true)
        val sound = prefs.getBoolean("sound_enabled", true)
        return SalaryConfig(
            monthlyGrossSalary = gross,
            weeklyHours = hours,
            hapticsEnabled = haptics,
            soundEnabled = sound
        )
    }

    fun updateSalary(gross: Double, weeklyHours: Double) {
        prefs.edit()
            .putFloat("monthly_gross", gross.toFloat())
            .putFloat("weekly_hours", weeklyHours.toFloat())
            .apply()
        _salaryConfig.value = _salaryConfig.value.copy(
            monthlyGrossSalary = gross,
            weeklyHours = weeklyHours
        )
    }

    fun toggleHaptics(enabled: Boolean) {
        prefs.edit().putBoolean("haptics_enabled", enabled).apply()
        _salaryConfig.value = _salaryConfig.value.copy(hapticsEnabled = enabled)
    }

    fun toggleSound(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _salaryConfig.value = _salaryConfig.value.copy(soundEnabled = enabled)
    }
}
