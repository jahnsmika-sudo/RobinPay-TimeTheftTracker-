package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val durationSeconds: Long,
    val earnedAmount: Double,
    val hourlySalaryRate: Double,
    val isMeeting: Boolean = false,
    val meetingParticipants: Int = 1,
    val meetingBurnedAmount: Double = 0.0,
    val notes: String = ""
)
