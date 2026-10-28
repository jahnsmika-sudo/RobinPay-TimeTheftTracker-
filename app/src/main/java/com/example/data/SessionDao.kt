package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startTimestamp DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE categoryId = :categoryId ORDER BY startTimestamp DESC")
    fun getSessionsByCategory(categoryId: String): Flow<List<SessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)

    @Query("DELETE FROM sessions")
    suspend fun clearAllSessions()

    @Query("SELECT SUM(earnedAmount) FROM sessions")
    fun getTotalEarned(): Flow<Double?>

    @Query("SELECT SUM(durationSeconds) FROM sessions")
    fun getTotalDurationSeconds(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM sessions")
    fun getTotalSessionCount(): Flow<Int>
}
