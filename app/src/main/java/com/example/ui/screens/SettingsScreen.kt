package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.RobinPayViewModel
import com.example.data.SalaryConfig
import com.example.data.SessionEntity
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: RobinPayViewModel,
    salaryConfig: SalaryConfig,
    sessions: List<SessionEntity>
) {
    val context = LocalContext.current
    var grossInput by remember(salaryConfig.monthlyGrossSalary) {
        mutableStateOf(salaryConfig.monthlyGrossSalary.toInt().toString())
    }
    var hoursInput by remember(salaryConfig.weeklyHours) {
        mutableStateOf(salaryConfig.weeklyHours.toInt().toString())
    }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "Gehaltsrechner & Parameter ⚙️",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Berechnungsbasis für sekundengenaue Auszeiten",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Salary Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Arbeitsvertrags-Daten",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Monthly Gross Salary Input
                OutlinedTextField(
                    value = grossInput,
                    onValueChange = {
                        grossInput = it
                        saveMessage = null
                    },
                    label = { Text("Monatliches Bruttogehalt (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("salary_input"),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { Text("€  ", fontWeight = FontWeight.Bold, color = Emerald400) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Weekly Hours Input
                OutlinedTextField(
                    value = hoursInput,
                    onValueChange = {
                        hoursInput = it
                        saveMessage = null
                    },
                    label = { Text("Wöchentliche Arbeitsstunden") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hours_input"),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { Text("h/Woche  ", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Salary Presets
                Text(
                    text = "Schnellauswahl nach Gehaltsstufe:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "2.500 €" to 2500.0,
                        "3.800 €" to 3800.0,
                        "5.000 €" to 5000.0,
                        "7.200 €" to 7200.0
                    ).forEach { (label, value) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Slate800)
                                .clickable {
                                    grossInput = value.toInt().toString()
                                    viewModel.updateSalary(value, hoursInput.toDoubleOrNull() ?: 40.0)
                                    saveMessage = "Gehalt auf $label aktualisiert!"
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Emerald400
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Save Button
                Button(
                    onClick = {
                        val gross = grossInput.toDoubleOrNull() ?: 3800.0
                        val hours = hoursInput.toDoubleOrNull() ?: 40.0
                        viewModel.updateSalary(gross, hours)
                        saveMessage = "Daten erfolgreich gespeichert!"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_settings_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald400, contentColor = Slate950),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Berechnung aktualisieren", fontWeight = FontWeight.Bold)
                }

                saveMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "✓ $msg",
                        color = Emerald400,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Live Rate Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Vergütungs-Staffelung in Echtzeit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                RateRow(label = "Monatliche Soll-Stunden:", value = "${String.format("%.1f", salaryConfig.monthlyHours)} Std.")
                RateRow(label = "Stundensatz (brutto):", value = "€ ${String.format("%.2f", salaryConfig.hourlyRate)} / Std.")
                RateRow(label = "Minutensatz:", value = "€ ${String.format("%.2f", salaryConfig.minuteRate)} / Min.")
                RateRow(label = "Sekundentakt:", value = "€ ${String.format("%.4f", salaryConfig.secondRate)} / Sek.", isHighlight = true)

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate800, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Formel: Monatsstunden ≈ Wochenstunden × 4.3333\nLohn/Sek = (Bruttogehalt / Monatsstunden) / 3600",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Feedback & App Preferences
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "App-Einstellungen",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Vibration, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Haptisches Feedback", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "Vibration bei Start/Stopp & Meilensteinen", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Switch(
                        checked = salaryConfig.hapticsEnabled,
                        onCheckedChange = { viewModel.toggleHaptics(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Emerald400, checkedTrackColor = Slate800)
                    )
                }
            }
        }

        // Export Data / Backup
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Daten-Export & Archiv 📁",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Exportiere deine Auszeiten als CSV-Tabelle für eigene Auswertungen.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val csv = generateCsvExport(sessions)
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, csv)
                            putExtra(Intent.EXTRA_SUBJECT, "RobinPay_Auszeiten_Export.csv")
                            type = "text/csv"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "CSV exportieren"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("export_csv_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Auszeiten als CSV exportieren", fontSize = 13.sp)
                }
            }
        }

        // Satirical Disclaimer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate950),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Emerald400,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Satire- & Rechtshinweis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "RobinPay ist ein humorvoller Pausen- & Meeting-Kosten-Rechner. Alle Berechnungen dienen Unterhaltungszwecken. Der Entwickler haftet nicht für plötzliche Beförderungen durch überlegene Kaffeepausen-Netzwerke.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
    }
}

@Composable
fun RateRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = if (isHighlight) 14.sp else 12.sp,
            fontWeight = if (isHighlight) FontWeight.Black else FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (isHighlight) Emerald400 else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun generateCsvExport(sessions: List<SessionEntity>): String {
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN)
    val sb = StringBuilder()
    sb.append("ID;Kategorie;Start;Ende;Dauer_Sekunden;Betrag_EUR;Stundensatz_EUR;Meeting;Teilnehmer;Konzern_Burn_EUR;Notizen\n")
    sessions.forEach { s ->
        val startStr = df.format(Date(s.startTimestamp))
        val endStr = df.format(Date(s.endTimestamp))
        sb.append("${s.id};${s.categoryId};$startStr;$endStr;${s.durationSeconds};${String.format(Locale.US, "%.2f", s.earnedAmount)};${String.format(Locale.US, "%.2f", s.hourlySalaryRate)};${s.isMeeting};${s.meetingParticipants};${String.format(Locale.US, "%.2f", s.meetingBurnedAmount)};\"${s.notes.replace("\"", "\"\"")}\"\n")
    }
    return sb.toString()
}
