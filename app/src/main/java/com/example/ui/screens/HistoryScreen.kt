package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.RobinPayViewModel
import com.example.data.SalaryConfig
import com.example.data.SessionEntity
import com.example.model.AppLanguage
import com.example.model.BreakCategory
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: RobinPayViewModel,
    sessions: List<SessionEntity>,
    totalEarned: Double,
    totalDurationSeconds: Long,
    salaryConfig: SalaryConfig
) {
    val isDe = salaryConfig.language == AppLanguage.GERMAN
    val curr = salaryConfig.currencySymbol
    val lang = salaryConfig.language

    var selectedCategoryFilter by remember { mutableStateOf<String?>("all") }
    var sessionToDelete by remember { mutableStateOf<SessionEntity?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val filteredSessions = remember(sessions, selectedCategoryFilter) {
        if (selectedCategoryFilter == "all" || selectedCategoryFilter == null) {
            sessions
        } else {
            sessions.filter { it.categoryId == selectedCategoryFilter }
        }
    }

    val totalHours = totalDurationSeconds / 3600
    val totalMins = (totalDurationSeconds % 3600) / 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("history_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isDe) "Auszeiten-Historie 📜" else "Break History 📜",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isDe) "${sessions.size} erfasste Dienstgänge" else "${sessions.size} recorded sessions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (sessions.isNotEmpty()) {
                IconButton(
                    onClick = { showClearAllDialog = true },
                    modifier = Modifier.testTag("clear_all_history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = if (isDe) "Historie leeren" else "Clear History",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Key Metric Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isDe) "Gesamtbeute" else "Total Recuperated",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$curr ${String.format("%.2f", totalEarned)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Emerald400
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isDe) "Auszeit gesamt" else "Total Break Time",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${totalHours}h ${totalMins}m",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Category Breakdown Canvas Chart
        if (sessions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            CategoryBreakdownCard(sessions = sessions, lang = lang)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategoryFilter == "all",
                    onClick = { selectedCategoryFilter = "all" },
                    label = { Text(if (isDe) "Alle (${sessions.size})" else "All (${sessions.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Emerald400,
                        selectedLabelColor = Color.Black
                    )
                )
            }
            items(BreakCategory.entries) { category ->
                val count = sessions.count { it.categoryId == category.id }
                FilterChip(
                    selected = selectedCategoryFilter == category.id,
                    onClick = { selectedCategoryFilter = category.id },
                    label = { Text("${category.emoji} ${category.getTitle(lang)} ($count)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = category.color.copy(alpha = 0.3f),
                        selectedLabelColor = category.color
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sessions List
        if (filteredSessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🛋️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (sessions.isEmpty()) {
                            if (isDe) "Noch keine Auszeiten verbucht!" else "No breaks recorded yet!"
                        } else {
                            if (isDe) "Keine Einträge für diesen Filter." else "No records for this category."
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (sessions.isEmpty()) {
                            if (isDe) "Starte deine erste bezahlte Pause im Tracker." else "Start your first paid break session in the Tracker."
                        } else {
                            if (isDe) "Wähle einen anderen Filter aus." else "Select another filter to view records."
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredSessions, key = { it.id }) { session ->
                    val category = BreakCategory.fromId(session.categoryId)
                    SessionItemCard(
                        session = session,
                        category = category,
                        currencySymbol = curr,
                        lang = lang,
                        onDelete = { sessionToDelete = session }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
                }
            }
        }
    }

    // Delete Single Confirmation Dialog
    sessionToDelete?.let { session ->
        AlertDialog(
            onDismissRequest = { sessionToDelete = null },
            title = { Text(if (isDe) "Eintrag löschen?" else "Delete Entry?") },
            text = {
                Text(
                    if (isDe) {
                        "Möchtest du diese Auszeit ($curr ${String.format("%.2f", session.earnedAmount)}) unwiderruflich aus der Abrechnung entfernen?"
                    } else {
                        "Permanently remove this break session ($curr ${String.format("%.2f", session.earnedAmount)}) from your history?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSession(session.id)
                        sessionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                ) {
                    Text(if (isDe) "Löschen" else "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { sessionToDelete = null }) {
                    Text(if (isDe) "Abbrechen" else "Cancel")
                }
            }
        )
    }

    // Clear All Confirmation Dialog
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text(if (isDe) "Gesamte Historie leeren?" else "Clear Entire History?") },
            text = {
                Text(
                    if (isDe) {
                        "Alle Auszeit-Einträge und Berechnungen werden gelöscht. Dein Rang wird zurückgesetzt."
                    } else {
                        "All break entries will be deleted. Career progress will reset to Intern."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                ) {
                    Text(if (isDe) "Alles löschen" else "Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) {
                    Text(if (isDe) "Abbrechen" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryBreakdownCard(sessions: List<SessionEntity>, lang: AppLanguage) {
    val totalTime = sessions.sumOf { it.durationSeconds }.coerceAtLeast(1L)
    val categoryDurations = BreakCategory.entries.map { cat ->
        val duration = sessions.filter { it.categoryId == cat.id }.sumOf { it.durationSeconds }
        cat to duration
    }.filter { it.second > 0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donut Chart Canvas
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    var currentStartAngle = -90f
                    categoryDurations.forEach { (cat, duration) ->
                        val sweepAngle = (duration.toFloat() / totalTime.toFloat()) * 360f
                        drawArc(
                            color = cat.color,
                            startAngle = currentStartAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = 16f)
                        )
                        currentStartAngle += sweepAngle
                    }
                }
                Text(
                    text = "📊",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Legend
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (lang == AppLanguage.GERMAN) "Auszeiten-Verteilung" else "Break Time Distribution",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                categoryDurations.take(3).forEach { (cat, duration) ->
                    val percentage = (duration.toFloat() / totalTime.toFloat() * 100).toInt()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(cat.color)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${cat.emoji} ${cat.getTitle(lang)}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$percentage%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = cat.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SessionItemCard(
    session: SessionEntity,
    category: BreakCategory,
    currencySymbol: String,
    lang: AppLanguage,
    onDelete: () -> Unit
) {
    val locale = if (lang == AppLanguage.GERMAN) Locale.GERMAN else Locale.US
    val dateFormatter = remember(lang) { SimpleDateFormat("dd. MMM, HH:mm", locale) }
    val formattedDate = remember(session.startTimestamp, lang) {
        dateFormatter.format(Date(session.startTimestamp))
    }

    val mins = session.durationSeconds / 60
    val secs = session.durationSeconds % 60
    val durationText = "${mins}m ${secs}s"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(category.color.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = category.emoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = category.getTitle(lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = formattedDate,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+ $currencySymbol ${String.format("%.2f", session.earnedAmount)}",
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp,
                            color = Emerald400
                        )
                        Text(
                            text = durationText,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = if (lang == AppLanguage.GERMAN) "Löschen" else "Delete",
                            tint = Slate700,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Meeting Burner Info
            if (session.isMeeting && session.meetingBurnedAmount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A153A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (lang == AppLanguage.GERMAN) {
                            "Konzern-Burn: $currencySymbol ${String.format("%.2f", session.meetingBurnedAmount)} (${session.meetingParticipants} Teilnehmer)"
                        } else {
                            "Company Burn: $currencySymbol ${String.format("%.2f", session.meetingBurnedAmount)} (${session.meetingParticipants} attendees)"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE9D5FF)
                    )
                }
            }

            // User Notes
            if (session.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💬 \"${session.notes}\"",
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
