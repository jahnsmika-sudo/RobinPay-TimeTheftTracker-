package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.RobinPayViewModel
import com.example.data.SalaryConfig
import com.example.model.AppLanguage
import com.example.model.LootEquivalent
import com.example.model.Rank
import com.example.model.RankProgress
import com.example.ui.theme.Amber400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900

@Composable
fun RanksScreen(
    viewModel: RobinPayViewModel,
    rankProgress: RankProgress,
    totalEarned: Double,
    salaryConfig: SalaryConfig
) {
    val context = LocalContext.current
    val isDe = salaryConfig.language == AppLanguage.GERMAN
    val curr = salaryConfig.currencySymbol
    val lang = salaryConfig.language

    var showReceiptDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("ranks_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isDe) "Beute & Karriere-Ränge 🏆" else "Loot & Career Ranks 🏆",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isDe) "Vom Praktikanten zum Robin Hood der Wirtschaft" else "From Sneaky Intern to Robin Hood of Capitalism",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Hero Card: Current Rank
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_rank_card"),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Amber400)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .background(Amber400.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, Amber400, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = rankProgress.currentRank.emoji, fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isDe) "Aktueller Rang:" else "Current Rank:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = rankProgress.currentRank.getTitle(lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Amber400
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "\"${rankProgress.currentRank.getQuote(lang)}\"",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress to Next Rank
                    if (rankProgress.nextRank != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isDe) {
                                    "Nächster Rang: ${rankProgress.nextRank.emoji} ${rankProgress.nextRank.getTitle(lang)}"
                                } else {
                                    "Next Rank: ${rankProgress.nextRank.emoji} ${rankProgress.nextRank.getTitle(lang)}"
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${(rankProgress.progressToNext * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { rankProgress.progressToNext },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Emerald400,
                            trackColor = Slate700
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isDe) {
                                "Noch $curr ${String.format("%.2f", rankProgress.remainingAmount)} bis zum Aufstieg"
                            } else {
                                "Only $curr ${String.format("%.2f", rankProgress.remainingAmount)} left to level up"
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = if (isDe) "👑 Maximaler Rang erreicht! Du bist eine Legende." else "👑 Maximum rank achieved! You are a workplace legend.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald400
                        )
                    }
                }
            }
        }

        // Slacking-Wrapped Button
        item {
            Button(
                onClick = { showReceiptDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("slacking_wrapped_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald400, contentColor = Color.Black),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isDe) "Abrechnungs-Beleg generieren & teilen" else "Generate & Share Slacking Receipt",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Section: Real-Value Loot
        item {
            Text(
                text = if (isDe) "Erarbeitete Naturalien (Sachwerte) 🌯☕" else "Office Loot Equivalents 🌯☕",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isDe) {
                    "Was du durch bezahlte Pausen bisher im echten Leben refinanziert hast:"
                } else {
                    "Real-world milestones unlocked by recuperating corporate time:"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(LootEquivalent.ALL_LOOT.size) { index ->
            val loot = LootEquivalent.ALL_LOOT[index]
            val countEarned = totalEarned / loot.cost
            val isUnlocked = countEarned >= 1.0

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isUnlocked) Emerald400.copy(alpha = 0.5f) else Slate800
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = loot.emoji, fontSize = 32.sp, modifier = Modifier.padding(end = 12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = loot.getName(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$curr ${String.format("%.2f", loot.cost)}",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = loot.getFunDescription(lang),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isDe) {
                                "${String.format("%.1f", countEarned)} ${loot.getUnitLabel(lang)} freigeschaltet"
                            } else {
                                "${String.format("%.1f", countEarned)} ${loot.getUnitLabel(lang)} unlocked"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) Emerald400 else Amber400
                        )
                    }
                }
            }
        }

        // Section: All Career Ranks Ladder
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isDe) "Die Karriereleiter 🧗" else "The Career Ladder 🧗",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(Rank.entries.size) { index ->
            val rank = Rank.entries[index]
            val isCurrent = rankProgress.currentRank == rank
            val isUnlocked = totalEarned >= rank.minEarnings

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isCurrent) Slate850 else Slate900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    if (isCurrent) 2.dp else 1.dp,
                    if (isCurrent) Amber400 else if (isUnlocked) Emerald400.copy(alpha = 0.4f) else Slate800
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = rank.emoji, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = rank.getTitle(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isCurrent) Amber400 else MaterialTheme.colorScheme.onSurface
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Amber400.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isDe) "AKTUELL" else "CURRENT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber400
                                    )
                                }
                            }
                        }

                        Text(
                            text = rank.getDescription(lang),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (isDe) {
                                "Ab $curr ${String.format("%.0f", rank.minEarnings)} Auszeit-Verdienst"
                            } else {
                                "Unlocked at $curr ${String.format("%.0f", rank.minEarnings)} break earnings"
                            },
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) Emerald400 else Slate700,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
        }
    }

    // Slacking-Wrapped / Receipt Dialog
    if (showReceiptDialog) {
        val receiptText = viewModel.generateShareableReceiptText()

        AlertDialog(
            onDismissRequest = { showReceiptDialog = false },
            title = {
                Text(
                    text = if (isDe) "🧾 Offizieller Ausgleichs-Beleg" else "🧾 Official Recuperation Receipt",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFFF8FAFC)
                    ) {
                        Text(
                            text = receiptText,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, receiptText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, if (isDe) "Abrechnung teilen" else "Share Receipt"))
                        showReceiptDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald400, contentColor = Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isDe) "Teilen" else "Share")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReceiptDialog = false }) {
                    Text(if (isDe) "Schließen" else "Close")
                }
            }
        )
    }
}
