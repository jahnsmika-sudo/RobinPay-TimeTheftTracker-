package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Ultra-realistic Microsoft Excel Theme Colors
private val ExcelDarkGreen = Color(0xFF107C41)
private val ExcelHeaderGreen = Color(0xFF185ABD)
private val ExcelGridLine = Color(0xFFD4D4D4)
private val ExcelHeaderBg = Color(0xFFF2F2F2)
private val ExcelFormulaBg = Color(0xFFFFFFFF)
private val ExcelActiveTab = Color(0xFFFFFFFF)
private val ExcelInactiveTab = Color(0xFFE6E6E6)
private val ExcelRowNumberBg = Color(0xFFF3F3F3)

data class ExcelRowData(
    val costCenter: String,
    val description: String,
    val q1Actual: String,
    val q2Actual: String,
    val q3Forecast: String,
    val variance: String,
    val isPositiveVariance: Boolean = true,
    val isTotal: Boolean = false
)

@Composable
fun BossScreen(
    onDismiss: () -> Unit
) {
    // Intercept back button to safely dismiss the Boss screen
    BackHandler {
        onDismiss()
    }

    var selectedTab by remember { mutableStateOf(0) }
    var showExitHint by remember { mutableStateOf(false) }

    val rows = remember {
        listOf(
            ExcelRowData("4100", "IT-Infrastruktur Server", "45.200 €", "48.150 €", "49.000 €", "+1.7%", true),
            ExcelRowData("4120", "Cloud Hosting AWS/Azure", "112.400 €", "119.800 €", "125.000 €", "+4.3%", true),
            ExcelRowData("4200", "Software-Lizenzen Enterprise", "84.300 €", "84.300 €", "86.000 €", "+2.0%", true),
            ExcelRowData("4310", "Externe Beratung & Audits", "62.000 €", "58.400 €", "55.000 €", "-5.8%", false),
            ExcelRowData("4450", "Hardware Leasing Workstations", "29.750 €", "30.100 €", "31.000 €", "+2.9%", true),
            ExcelRowData("4500", "Telekommunikation & VPN", "14.200 €", "14.150 €", "14.500 €", "+2.4%", true),
            ExcelRowData("4620", "Security Monitoring / SOC", "36.800 €", "37.200 €", "38.000 €", "+2.1%", true),
            ExcelRowData("4700", "Mitarbeiter Weiterbildung", "18.500 €", "21.300 €", "22.500 €", "+5.6%", true),
            ExcelRowData("4810", "Reisekosten & Tagungen", "24.100 €", "26.400 €", "25.000 €", "-5.3%", false),
            ExcelRowData("4900", "Bürobedarf & Facility", "9.600 €", "10.200 €", "10.500 €", "+2.9%", true),
            ExcelRowData("5100", "Abschreibungen CAPEX", "78.900 €", "79.200 €", "80.000 €", "+1.0%", true),
            ExcelRowData("TOTAL", "Konzern-Gesamtaufwand (OPEX)", "515.750 €", "529.200 €", "546.500 €", "+3.2%", true, isTotal = true)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .testTag("boss_screen")
    ) {
        // Excel Top Title Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ExcelDarkGreen)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TableChart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Q3_Konzern_Budget_CAPEX_FINAL_v3.xlsx",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AutoSpeichern Ein • In SharePoint gespeichert",
                        color = Color(0xFFD1FAE5),
                        fontSize = 10.sp
                    )
                }
            }

            // Discreet exit button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(32.dp)
                    .testTag("boss_exit_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Schließen",
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Ribbon Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ExcelHeaderBg)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf("Datei", "Start", "Einfügen", "Zeichnen", "Formeln", "Daten", "Ansicht").forEachIndexed { idx, tab ->
                Text(
                    text = tab,
                    fontSize = 11.sp,
                    fontWeight = if (idx == 1) FontWeight.Bold else FontWeight.Normal,
                    color = if (idx == 1) ExcelDarkGreen else Color.DarkGray,
                    modifier = Modifier.clickable { }
                )
            }
        }

        // Formula Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ExcelFormulaBg)
                .border(width = 0.5.dp, color = ExcelGridLine)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(2.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "D14",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "fx",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "=SUMME(E4:E13) * (1 + EBITDA_Surcharge)",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )
        }

        // Horizontal scroll container for the spreadsheet table
        val horizontalScrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
        ) {
            Column(
                modifier = Modifier.width(760.dp)
            ) {
                // Table Column Headers (A, B, C, D, E, F)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ExcelHeaderBg)
                        .border(0.5.dp, ExcelGridLine)
                ) {
                    TableCell(text = "#", width = 36.dp, isHeader = true)
                    TableCell(text = "A (KSt)", width = 64.dp, isHeader = true)
                    TableCell(text = "B (Kostenstellen-Bezeichnung)", width = 230.dp, isHeader = true)
                    TableCell(text = "C (Q1 Ist)", width = 100.dp, isHeader = true)
                    TableCell(text = "D (Q2 Ist)", width = 100.dp, isHeader = true)
                    TableCell(text = "E (Q3 Forecast)", width = 110.dp, isHeader = true)
                    TableCell(text = "F (Varianz)", width = 120.dp, isHeader = true)
                }

                // Table Rows
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(rows) { index, row ->
                        val isEven = index % 2 == 0
                        val rowBg = if (row.isTotal) Color(0xFFFEF3C7) else if (isEven) Color.White else Color(0xFFFAFAFA)
                        val textWeight = if (row.isTotal) FontWeight.Bold else FontWeight.Normal

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(rowBg)
                                .border(0.5.dp, ExcelGridLine)
                        ) {
                            // Row Index
                            TableCell(
                                text = "${index + 4}",
                                width = 36.dp,
                                isRowHeader = true
                            )
                            // Col A
                            TableCell(
                                text = row.costCenter,
                                width = 64.dp,
                                fontWeight = textWeight
                            )
                            // Col B
                            TableCell(
                                text = row.description,
                                width = 230.dp,
                                fontWeight = textWeight
                            )
                            // Col C
                            TableCell(
                                text = row.q1Actual,
                                width = 100.dp,
                                alignRight = true,
                                fontWeight = textWeight
                            )
                            // Col D
                            TableCell(
                                text = row.q2Actual,
                                width = 100.dp,
                                alignRight = true,
                                fontWeight = textWeight
                            )
                            // Col E
                            TableCell(
                                text = row.q3Forecast,
                                width = 110.dp,
                                alignRight = true,
                                fontWeight = textWeight,
                                textColor = if (row.isTotal) ExcelDarkGreen else Color.Black
                            )
                            // Col F
                            TableCell(
                                text = row.variance,
                                width = 120.dp,
                                alignRight = true,
                                fontWeight = textWeight,
                                textColor = if (row.isPositiveVariance) Color(0xFF047857) else Color(0xFFB91C1C)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Sheet Tab bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ExcelInactiveTab)
                .border(0.5.dp, ExcelGridLine)
                .padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("CAPEX_OPEX_Übersicht", "Personal_Headcount", "CashFlow_Forecast").forEachIndexed { index, name ->
                val active = selectedTab == index
                Box(
                    modifier = Modifier
                        .background(
                            if (active) ExcelActiveTab else ExcelInactiveTab,
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                        .border(
                            0.5.dp,
                            if (active) ExcelDarkGreen else Color.Transparent,
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                        .clickable { selectedTab = index }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 11.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        color = if (active) ExcelDarkGreen else Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Unobtrusive return text button in the status bar
            Box(
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Bereit 100% • [Beenden]",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    isHeader: Boolean = false,
    isRowHeader: Boolean = false,
    alignRight: Boolean = false,
    fontWeight: FontWeight = FontWeight.Normal,
    textColor: Color = Color.Black
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(26.dp)
            .background(if (isRowHeader) ExcelRowNumberBg else Color.Transparent)
            .border(0.5.dp, ExcelGridLine)
            .padding(horizontal = 4.dp, vertical = 3.dp),
        contentAlignment = if (alignRight) Alignment.CenterEnd else if (isRowHeader || isHeader) Alignment.Center else Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 10.5.sp,
            fontWeight = if (isHeader) FontWeight.Bold else fontWeight,
            color = if (isHeader || isRowHeader) Color.DarkGray else textColor,
            textAlign = if (alignRight) TextAlign.End else TextAlign.Start,
            fontFamily = if (alignRight || isRowHeader) FontFamily.Monospace else FontFamily.SansSerif,
            maxLines = 1
        )
    }
}
