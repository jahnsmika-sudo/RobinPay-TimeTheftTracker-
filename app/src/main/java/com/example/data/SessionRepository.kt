package com.example.data

import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {
    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()
    val totalEarned: Flow<Double?> = sessionDao.getTotalEarned()
    val totalDurationSeconds: Flow<Long?> = sessionDao.getTotalDurationSeconds()
    val totalSessionCount: Flow<Int> = sessionDao.getTotalSessionCount()

    suspend fun insertSession(session: SessionEntity): Long = sessionDao.insertSession(session)
    suspend fun deleteSession(id: Long) = sessionDao.deleteSessionById(id)
    suspend fun clearAll() = sessionDao.clearAllSessions()
}
