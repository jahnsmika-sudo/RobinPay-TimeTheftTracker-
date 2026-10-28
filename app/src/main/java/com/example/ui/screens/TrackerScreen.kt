package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ActiveSessionState
import com.example.RobinPayViewModel
import com.example.data.SalaryConfig
import com.example.model.AppLanguage
import com.example.model.BreakCategory
import com.example.model.MilestoneProgress
import com.example.ui.theme.Amber400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun TrackerScreen(
    viewModel: RobinPayViewModel,
    activeSession: ActiveSessionState,
    salaryConfig: SalaryConfig,
    milestone: MilestoneProgress,
    onOpenBossMode: () -> Unit
) {
    val isDe = salaryConfig.language == AppLanguage.GERMAN
    val curr = salaryConfig.currencySymbol
    val lang = salaryConfig.language

    var showSaveDialog by remember { mutableStateOf(false) }
    var sessionNotes by remember { mutableStateOf("") }
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSickDayDialog by remember { mutableStateOf(false) }
    var sickDayHours by remember { mutableStateOf("8") }
    var sickDayNotes by remember {
        mutableStateOf(if (isDe) "Gelber Schein eingereicht" else "Doctor's note submitted")
    }
    var bookedSickDayMessage by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (activeSession.isTracking && !activeSession.isPaused) 1.04f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Rank & Hourly Rate Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RobinPay 💸",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isDe) {
                            "Lohn-Ticker: ${String.format("%.2f", salaryConfig.hourlyRate)} $curr/h • ${String.format("%.4f", salaryConfig.secondRate)} $curr/s"
                        } else {
                            "Rate: ${String.format("%.2f", salaryConfig.hourlyRate)} $curr/hr • ${String.format("%.4f", salaryConfig.secondRate)} $curr/sec"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Quick Boss-Key Emergency Header Button
                FilledTonalButton(
                    onClick = onOpenBossMode,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Rose500.copy(alpha = 0.2f),
                        contentColor = Rose500
                    ),
                    modifier = Modifier.testTag("top_boss_button")
                ) {
                    Text(
                        text = if (isDe) "🚨 CHEF!" else "🚨 BOSS!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Picker Carousel (when not tracking, or showing active category)
            Text(
                text = if (activeSession.isTracking) {
                    if (isDe) "Aktive Auszeit" else "Active Break Session"
                } else {
                    if (isDe) "Büro-Auszeit wählen" else "Select Office Break"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(BreakCategory.entries) { category ->
                    val isSelected = activeSession.category == category
                    CategoryCard(
                        category = category,
                        lang = lang,
                        isSelected = isSelected,
                        enabled = !activeSession.isTracking,
                        onClick = { viewModel.selectCategory(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Live Ticker Display Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(if (activeSession.isTracking && !activeSession.isPaused) pulseScale else 1f)
                    .testTag("live_ticker_card"),
                colors = CardDefaults.cardColors(
                    containerColor = Slate900
                ),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = if (activeSession.isTracking) activeSession.category.color else Slate700
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Category Badge inside Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                activeSession.category.color.copy(alpha = 0.15f),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = activeSession.category.emoji,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = activeSession.category.getTitle(lang),
                            fontWeight = FontWeight.Bold,
                            color = activeSession.category.color,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Cash Earned Counter
                    Text(
                        text = if (isDe) "Verdient während Auszeit" else "Earned During Break",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$curr ",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald400,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = String.format("%.4f", activeSession.earnedAmount),
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Timer Duration (HH:MM:SS)
                    val hours = activeSession.elapsedSeconds / 3600
                    val minutes = (activeSession.elapsedSeconds % 3600) / 60
                    val seconds = activeSession.elapsedSeconds % 60
                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    Text(
                        text = if (isDe) "Dauer: $timeString" else "Duration: $timeString",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        color = if (activeSession.isPaused) Amber400 else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (activeSession.isPaused) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isDe) "⏸ PAUSIERT" else "⏸ PAUSED",
                            color = Amber400,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Satirical Quote for current category
                    val quotes = activeSession.category.getQuotes(lang)
                    val quote = quotes.getOrNull(activeSession.quoteIndex % quotes.size) ?: quotes.first()
                    Text(
                        text = "\"$quote\"",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // Sinnlos-Meeting Corporate Burner Section (when Meeting is selected)
            AnimatedVisibility(
                visible = activeSession.category.isMeetingBurner,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("meeting_burner_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2A153A)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFA855F7))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFF97316),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDe) "Konzern-Geldverbrenner 🔥" else "Corporate Capital Burner 🔥",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE9D5FF),
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isDe) "Insgesamt verbranntes Konzernkapital:" else "Total burned company capital:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFD8B4FE)
                        )

                        Text(
                            text = "$curr ${String.format("%.2f", activeSession.meetingBurnedAmount)}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFFCD34D)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Participant Stepper
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFF3B1E54), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isDe) "Teilnehmer im Meeting:" else "Attendees:",
                                fontSize = 12.sp,
                                color = Color(0xFFE9D5FF)
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            IconButton(
                                onClick = { viewModel.setMeetingParticipants(activeSession.meetingParticipants - 1) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Minus",
                                    tint = Color.White
                                )
                            }

                            Text(
                                text = "${activeSession.meetingParticipants}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            IconButton(
                                onClick = { viewModel.setMeetingParticipants(activeSession.meetingParticipants + 1) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Plus",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Sick Leave (Krankheitstag) Quick Booking Section
            AnimatedVisibility(
                visible = activeSession.category == BreakCategory.SICK_LEAVE && !activeSession.isTracking,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sick_leave_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2E2B)),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF14B8A6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🤒", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isDe) "Lohnfortzahlung / Gelber Schein" else "Paid Sick Leave & Recovery",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCCFBF1),
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isDe) {
                                "100% gesetzliche Lohnfortzahlung (§ 3 EntgFG). Du kannst entweder die Auszeit live laufen lassen oder direkt einen ganzen Arbeitstag verbuchen."
                            } else {
                                "Statutory paid sick leave. Track your recovery live or record a full 8-hour workday immediately."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF99F6E4),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val dayPay = 8.0 * salaryConfig.hourlyRate
                        Button(
                            onClick = { showSickDayDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF14B8A6),
                                contentColor = Slate950
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("book_sick_day_button")
                        ) {
                            Text(
                                text = if (isDe) {
                                    "Ganzen Tag buchen (8h = $curr ${String.format("%.2f", dayPay)})"
                                } else {
                                    "Book Full Day (8h = $curr ${String.format("%.2f", dayPay)})"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        bookedSickDayMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "✓ $msg",
                                color = Color(0xFF5EEAD4),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real-Value Milestone Mini Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_card"),
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
                    Text(
                        text = milestone.targetLoot.emoji,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isDe) {
                                    "Nächstes Äquivalent: ${milestone.targetLoot.getName(lang)}"
                                } else {
                                    "Next Loot: ${milestone.targetLoot.getName(lang)}"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${milestone.percent}%",
                                fontWeight = FontWeight.Bold,
                                color = Emerald400,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { milestone.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Emerald400,
                            trackColor = Slate700
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isDe) {
                                "Noch $curr ${String.format("%.2f", milestone.remainingCost)} bis zum Ziel ($curr ${milestone.targetLoot.cost})"
                            } else {
                                "Only $curr ${String.format("%.2f", milestone.remainingCost)} left to goal ($curr ${milestone.targetLoot.cost})"
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Control Buttons (Start / Pause / Resume / Save)
            if (!activeSession.isTracking) {
                Button(
                    onClick = { viewModel.startTracking() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("start_tracking_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald400,
                        contentColor = Slate950
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isDe) "AUSZEIT STARTEN" else "START BREAK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pause / Resume Button
                    FilledTonalButton(
                        onClick = {
                            if (activeSession.isPaused) viewModel.resumeTracking()
                            else viewModel.pauseTracking()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("pause_resume_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Slate700,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (activeSession.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (activeSession.isPaused) {
                                if (isDe) "Weiter" else "Resume"
                            } else {
                                if (isDe) "Pause" else "Pause"
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Finish & Save Button
                    Button(
                        onClick = { showSaveDialog = true },
                        modifier = Modifier
                            .weight(1.4f)
                            .height(52.dp)
                            .testTag("stop_save_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Emerald400,
                            contentColor = Slate950
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isDe) "Speichern" else "Save & Log",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Cancel Button
                    OutlinedButton(
                        onClick = { showCancelDialog = true },
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("cancel_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Rose500)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = if (isDe) "Abbrechen" else "Cancel",
                            tint = Rose500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // space for bottom navigation & FAB
        }

        // Floating Emergency Boss-Key Alarm Button (Bottom Right)
        FloatingActionButton(
            onClick = onOpenBossMode,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("boss_key_fab"),
            containerColor = Rose500,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🚨", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDe) "CHEF!" else "BOSS!",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }
    }

    // Save Session Dialog with optional notes
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = {
                Text(
                    text = if (isDe) "Auszeit abrechnen 🎉" else "Log Break Session 🎉",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isDe) {
                            "Du hast erfolgreich $curr ${String.format("%.2f", activeSession.earnedAmount)} auf Firmenkosten erwirtschaftet!"
                        } else {
                            "You earned $curr ${String.format("%.2f", activeSession.earnedAmount)} during your break time!"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = sessionNotes,
                        onValueChange = { sessionNotes = it },
                        label = { Text(if (isDe) "Notiz (optional)" else "Note (optional)") },
                        placeholder = { Text(if (isDe) "z.B. IT-Update / Flurfunk mit Marketing" else "e.g. Brainstorming / Watercooler chat") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.stopAndSaveTracking(sessionNotes)
                        sessionNotes = ""
                        showSaveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald400, contentColor = Slate950)
                ) {
                    Text(if (isDe) "In Historie buchen" else "Save to History")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text(if (isDe) "Weiter tracken" else "Keep Tracking")
                }
            }
        )
    }

    // Cancel Session Confirmation Dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text(if (isDe) "Auszeit verwerfen?" else "Discard Break?") },
            text = {
                Text(
                    if (isDe) {
                        "Möchtest du diese Session wirklich ohne Speichern abbrechen? Die erbeuteten Beträge gehen verloren."
                    } else {
                        "Do you really want to discard this break session without saving?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelTracking()
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500, contentColor = Color.White)
                ) {
                    Text(if (isDe) "Verwerfen" else "Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text(if (isDe) "Abbrechen" else "Cancel")
                }
            }
        )
    }

    // Book Full Sick Day Dialog
    if (showSickDayDialog) {
        val parsedHours = sickDayHours.toDoubleOrNull() ?: 8.0
        val earnedAmount = parsedHours * salaryConfig.hourlyRate

        AlertDialog(
            onDismissRequest = { showSickDayDialog = false },
            title = {
                Text(
                    text = if (isDe) "Krankheitstag verbuchen 🤒" else "Log Full Sick Day 🤒",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isDe) {
                            "Erfasse einen regulären Arbeitstag mit 100% gesetzlicher Lohnfortzahlung:"
                        } else {
                            "Record a standard working day covered by statutory paid sick leave:"
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = sickDayHours,
                        onValueChange = { sickDayHours = it },
                        label = { Text(if (isDe) "Arbeitsstunden" else "Working Hours") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = sickDayNotes,
                        onValueChange = { sickDayNotes = it },
                        label = { Text(if (isDe) "Notiz / Attest" else "Doctor's Note / Details") },
                        placeholder = { Text(if (isDe) "z.B. Attest beim Arbeitgeber eingereicht" else "e.g. Medical certificate filed") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isDe) {
                            "Berechnetes Entgelt: $curr ${String.format("%.2f", earnedAmount)}"
                        } else {
                            "Total Compensation: $curr ${String.format("%.2f", earnedAmount)}"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald400
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val hours = sickDayHours.toDoubleOrNull() ?: 8.0
                        viewModel.bookFullSickDay(hours, sickDayNotes)
                        bookedSickDayMessage = if (isDe) {
                            "$hours Stunden ($curr ${String.format("%.2f", hours * salaryConfig.hourlyRate)}) erfolgreich in Historie gebucht!"
                        } else {
                            "$hours hours ($curr ${String.format("%.2f", hours * salaryConfig.hourlyRate)}) booked to history!"
                        }
                        showSickDayDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald400, contentColor = Slate950)
                ) {
                    Text(if (isDe) "In Historie verbuchen" else "Book to History")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSickDayDialog = false }) {
                    Text(if (isDe) "Abbrechen" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryCard(
    category: BreakCategory,
    lang: AppLanguage,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) category.color else Slate700
    val bgColor = if (isSelected) category.color.copy(alpha = 0.2f) else Slate850

    Box(
        modifier = Modifier
            .width(112.dp)
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.emoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.getTitle(lang),
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) category.color else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
