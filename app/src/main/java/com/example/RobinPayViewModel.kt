package com.example

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RobinPayDatabase
import com.example.data.SalaryConfig
import com.example.data.SessionEntity
import com.example.data.SessionRepository
import com.example.data.UserPreferencesRepository
import com.example.model.BreakCategory
import com.example.model.LootEquivalent
import com.example.model.MilestoneProgress
import com.example.model.Rank
import com.example.model.RankProgress
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.model.AppLanguage

enum class AppTab(val titleEn: String, val titleDe: String, val iconName: String) {
    TRACKER("Tracker", "Tracker", "timer"),
    HISTORY("History", "Historie", "history"),
    LOOT_RANKS("Ranks", "Ränge", "trophy"),
    SETTINGS("Settings", "Rechner", "settings");

    fun getTitle(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) titleDe else titleEn
}

data class ActiveSessionState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val category: BreakCategory = BreakCategory.RESTROOM,
    val elapsedSeconds: Long = 0L,
    val accumulatedMillis: Long = 0L,
    val lastResumeTime: Long = 0L,
    val earnedAmount: Double = 0.0,
    val meetingParticipants: Int = 6,
    val meetingBurnedAmount: Double = 0.0,
    val sessionStartTime: Long = 0L,
    val quoteIndex: Int = 0
)

class RobinPayViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionDao = RobinPayDatabase.getDatabase(application).sessionDao()
    private val sessionRepository = SessionRepository(sessionDao)
    private val prefsRepository = UserPreferencesRepository(application)

    val salaryConfig: StateFlow<SalaryConfig> = prefsRepository.salaryConfig

    val allSessions: StateFlow<List<SessionEntity>> = sessionRepository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalEarnedFromHistory: StateFlow<Double> = sessionRepository.totalEarned
        .combine(MutableStateFlow(0.0)) { earned, _ -> earned ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalDurationSeconds: StateFlow<Long> = sessionRepository.totalDurationSeconds
        .combine(MutableStateFlow(0L)) { duration, _ -> duration ?: 0L }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _currentTab = MutableStateFlow(AppTab.TRACKER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _activeSession = MutableStateFlow(ActiveSessionState())
    val activeSession: StateFlow<ActiveSessionState> = _activeSession.asStateFlow()

    private val _isBossModeActive = MutableStateFlow(false)
    val isBossModeActive: StateFlow<Boolean> = _isBossModeActive.asStateFlow()

    private var tickerJob: Job? = null

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun activateBossMode() {
        triggerHaptic(HapticType.EMERGENCY)
        _isBossModeActive.value = true
    }

    fun deactivateBossMode() {
        _isBossModeActive.value = false
    }

    fun selectCategory(category: BreakCategory) {
        if (!_activeSession.value.isTracking) {
            _activeSession.value = _activeSession.value.copy(
                category = category,
                quoteIndex = (0 until category.wittyQuotes.size).random()
            )
        }
    }

    fun setMeetingParticipants(count: Int) {
        val clamped = count.coerceIn(2, 100)
        _activeSession.value = _activeSession.value.copy(meetingParticipants = clamped)
    }

    fun startTracking() {
        triggerHaptic(HapticType.START)
        val now = System.currentTimeMillis()
        _activeSession.value = _activeSession.value.copy(
            isTracking = true,
            isPaused = false,
            elapsedSeconds = 0L,
            accumulatedMillis = 0L,
            lastResumeTime = now,
            earnedAmount = 0.0,
            meetingBurnedAmount = 0.0,
            sessionStartTime = now,
            quoteIndex = (0 until _activeSession.value.category.wittyQuotes.size).random()
        )
        startTicker()
    }

    fun pauseTracking() {
        triggerHaptic(HapticType.CLICK)
        val now = System.currentTimeMillis()
        val current = _activeSession.value
        val additionalMillis = if (current.lastResumeTime > 0) now - current.lastResumeTime else 0L
        _activeSession.value = current.copy(
            isPaused = true,
            accumulatedMillis = current.accumulatedMillis + additionalMillis,
            lastResumeTime = 0L
        )
        tickerJob?.cancel()
    }

    fun resumeTracking() {
        triggerHaptic(HapticType.CLICK)
        val now = System.currentTimeMillis()
        _activeSession.value = _activeSession.value.copy(
            isPaused = false,
            lastResumeTime = now
        )
        startTicker()
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (_activeSession.value.isTracking && !_activeSession.value.isPaused) {
                delay(100) // 10 updates per second for super smooth sub-cent ticker
                val now = System.currentTimeMillis()
                val current = _activeSession.value
                val currentChunk = if (current.lastResumeTime > 0) now - current.lastResumeTime else 0L
                val totalMillis = current.accumulatedMillis + currentChunk
                val totalSeconds = totalMillis / 1000L
                val exactSecondsDouble = totalMillis / 1000.0

                val cfg = salaryConfig.value
                val earned = exactSecondsDouble * cfg.secondRate
                val burned = if (current.category.isMeetingBurner) {
                    earned * current.meetingParticipants
                } else {
                    0.0
                }

                _activeSession.value = current.copy(
                    elapsedSeconds = totalSeconds,
                    earnedAmount = earned,
                    meetingBurnedAmount = burned
                )
            }
        }
    }

    fun bookFullSickDay(hours: Double = 8.0, notes: String = "Gelber Schein (Attest)") {
        triggerHaptic(HapticType.SUCCESS)
        val now = System.currentTimeMillis()
        val durationSecs = (hours * 3600.0).toLong()
        val earned = hours * salaryConfig.value.hourlyRate

        viewModelScope.launch {
            val entity = SessionEntity(
                categoryId = BreakCategory.SICK_LEAVE.id,
                startTimestamp = now - (durationSecs * 1000L),
                endTimestamp = now,
                durationSeconds = durationSecs,
                earnedAmount = earned,
                hourlySalaryRate = salaryConfig.value.hourlyRate,
                isMeeting = false,
                meetingParticipants = 1,
                meetingBurnedAmount = 0.0,
                notes = notes
            )
            sessionRepository.insertSession(entity)
        }
    }

    fun stopAndSaveTracking(notes: String = "") {
        triggerHaptic(HapticType.SUCCESS)
        val current = _activeSession.value
        tickerJob?.cancel()

        if (current.elapsedSeconds > 1 || current.earnedAmount > 0.01) {
            viewModelScope.launch {
                val entity = SessionEntity(
                    categoryId = current.category.id,
                    startTimestamp = current.sessionStartTime,
                    endTimestamp = System.currentTimeMillis(),
                    durationSeconds = current.elapsedSeconds,
                    earnedAmount = current.earnedAmount,
                    hourlySalaryRate = salaryConfig.value.hourlyRate,
                    isMeeting = current.category.isMeetingBurner,
                    meetingParticipants = current.meetingParticipants,
                    meetingBurnedAmount = current.meetingBurnedAmount,
                    notes = notes
                )
                sessionRepository.insertSession(entity)
            }
        }

        _activeSession.value = ActiveSessionState(category = current.category)
    }

    fun cancelTracking() {
        triggerHaptic(HapticType.CLICK)
        tickerJob?.cancel()
        _activeSession.value = ActiveSessionState(category = _activeSession.value.category)
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            sessionRepository.deleteSession(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            sessionRepository.clearAll()
        }
    }

    fun updateSalary(gross: Double, hours: Double) {
        prefsRepository.updateSalary(gross, hours)
    }

    fun toggleHaptics(enabled: Boolean) {
        prefsRepository.toggleHaptics(enabled)
    }

    fun toggleSound(enabled: Boolean) {
        prefsRepository.toggleSound(enabled)
    }

    // Helper to get total cumulative earnings (historical + live)
    fun getCombinedEarnings(): Double {
        return totalEarnedFromHistory.value + (_activeSession.value.earnedAmount)
    }

    fun getRankProgress(): RankProgress {
        return Rank.getRankFor(getCombinedEarnings())
    }

    fun getActiveMilestoneProgress(): MilestoneProgress {
        val amount = if (_activeSession.value.isTracking) {
            _activeSession.value.earnedAmount
        } else {
            getCombinedEarnings()
        }
        return LootEquivalent.findNextMilestone(amount)
    }

    enum class HapticType {
        CLICK, START, SUCCESS, EMERGENCY
    }

    private fun triggerHaptic(type: HapticType) {
        if (!salaryConfig.value.hapticsEnabled) return
        try {
            val app = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            vibrator?.let { v ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = when (type) {
                        HapticType.CLICK -> VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
                        HapticType.START -> VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 40), -1)
                        HapticType.SUCCESS -> VibrationEffect.createWaveform(longArrayOf(0, 50, 80, 80), -1)
                        HapticType.EMERGENCY -> VibrationEffect.createWaveform(longArrayOf(0, 100, 50, 100), -1)
                    }
                    v.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(50)
                }
            }
        } catch (_: Exception) {
            // gracefully fallback if vibration is not supported
        }
    }

    fun setLanguage(language: AppLanguage) {
        prefsRepository.setLanguage(language)
    }

    fun setCurrency(symbol: String) {
        prefsRepository.setCurrency(symbol)
    }

    fun generateShareableReceiptText(): String {
        val totalEarned = getCombinedEarnings()
        val lang = salaryConfig.value.language
        val curr = salaryConfig.value.currencySymbol
        val rank = getRankProgress().currentRank
        val totalSecs = totalDurationSeconds.value + _activeSession.value.elapsedSeconds
        val hours = totalSecs / 3600
        val mins = (totalSecs % 3600) / 60
        val donerCount = String.format("%.1f", totalEarned / 8.0)
        val coffeeCount = String.format("%.1f", totalEarned / 4.5)

        return if (lang == AppLanguage.GERMAN) {
            """
                🧾 ROBINPAY • OFFIZIELLER ZEITAUSGLEICHS-BELEG
                ------------------------------------------------
                Mitarbeiter-Status: ${rank.getTitle(lang)} ${rank.emoji}
                Erbeutetes Auszeit-Gehalt: $curr ${String.format("%.2f", totalEarned)}
                Verbrachte Inoffizielle Pause: ${hours}h ${mins}m
                
                Naturalien-Äquivalente:
                🥙 Döner Kebab: $donerCount Portionen
                ☕ Barista Flat White: $coffeeCount Tassen
                
                "${rank.getQuote(lang)}"
                
                Getrackt mit RobinPay – Der diskrete Büro-Pausen- & Meeting-Kosten-Rechner.
            """.trimIndent()
        } else {
            """
                🧾 ROBINPAY • OFFICIAL TIME-OFF RECUPERATION RECEIPT
                ----------------------------------------------------
                Employee Status: ${rank.getTitle(lang)} ${rank.emoji}
                Recuperated Break Earnings: $curr ${String.format("%.2f", totalEarned)}
                Time Spent on Inofficial Break: ${hours}h ${mins}m
                
                Loot Milestones Earned:
                🥙 Street Food Kebabs: $donerCount units
                ☕ Barista Lattes: $coffeeCount cups
                
                "${rank.getQuote(lang)}"
                
                Tracked with RobinPay – The Discrete Office Break & Meeting Cost Calculator.
            """.trimIndent()
        }
    }
}
