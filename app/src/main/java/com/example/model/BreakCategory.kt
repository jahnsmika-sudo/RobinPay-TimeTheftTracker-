package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CatAdmin
import com.example.ui.theme.CatCoffee
import com.example.ui.theme.CatIT
import com.example.ui.theme.CatMeeting
import com.example.ui.theme.CatSmoke
import com.example.ui.theme.CatToilet

enum class BreakCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val isMeetingBurner: Boolean = false,
    val wittyQuotes: List<String>
) {
    TOILET(
        id = "toilet",
        title = "Klogang",
        subtitle = "Das stille Örtchen / Royal Flush",
        emoji = "💩",
        color = CatToilet,
        wittyQuotes = listOf(
            "Boss makes a dollar, I make a dime, that's why I poop on company time.",
            "Strategische Tiefenreflexion auf Keramik.",
            "Goldgräber-Stimmung im Sanitärbereich.",
            "Körperliche Entlastung, finanzielle Bereicherung."
        )
    ),
    COFFEE(
        id = "coffee",
        title = "Flurfunk & Kaffee",
        subtitle = "Strategisches Koffein-Networking",
        emoji = "☕",
        color = CatCoffee,
        wittyQuotes = listOf(
            "Die Kaffeemaschine ist das wahre Intranet.",
            "Wichtiges Abstimmen von Gerüchten der Chefetage.",
            "Koffein tanken auf Firmenkosten.",
            "Ohne Bohnen keine Leistung."
        )
    ),
    SMOKE(
        id = "smoke",
        title = "Raucherpause",
        subtitle = "Frischluft-Exkursion & Lagebesprechung",
        emoji = "🚬",
        color = CatSmoke,
        wittyQuotes = listOf(
            "Draußen wird die wahre Unternehmenspolitik gemacht.",
            "Lungenbelüftung mit Blick auf den Parkplatz.",
            "Spontanes Stand-up-Meeting im Windkanal.",
            "Soziale Kontakte pflegen bei 8 Grad Nieselregen."
        )
    ),
    MEETING(
        id = "meeting",
        title = "Sinnlos-Meeting",
        subtitle = "Konzern-Koma & Geldverbrenner",
        emoji = "🥱",
        color = CatMeeting,
        isMeetingBurner = true,
        wittyQuotes = listOf(
            "Dieses Meeting hätte eine E-Mail sein können.",
            "Wir drehen uns im Kreis, aber mit schicker PowerPoint.",
            "Konzernkapital verbrennen im Minutentakt.",
            "Aktiv zuhören, innerlich den Feierabend planen."
        )
    ),
    IT_UPDATE(
        id = "it_update",
        title = "IT-Streik & Updates",
        subtitle = "Windows updatet... (37% abgeschlossen)",
        emoji = "⏳",
        color = CatIT,
        wittyQuotes = listOf(
            "Bitte schalten Sie den Computer nicht aus.",
            "Der IT-Helpdesk hat ein Ticket eröffnet. Priorität: Niedrig.",
            "Warten auf den Neustart ist bezahlte Kontemplation.",
            "Hardware streikt – Gewerkschaftliche Pause der Chips."
        )
    ),
    LIFE_ADMIN(
        id = "life_admin",
        title = "Life-Admin & Shopping",
        subtitle = "Private Marktrecherche & Paketstatus",
        emoji = "🛍️",
        color = CatAdmin,
        wittyQuotes = listOf(
            "Recherche nach günstigen Urlaubszielen während der Kernarbeitszeit.",
            "Der Paketbote klingelt in 12 Minuten.",
            "Wichtige persönliche Logistikoptimierung.",
            "Multitasking: Tabelle offen, Warenkorb voll."
        )
    );

    companion object {
        fun fromId(id: String): BreakCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: TOILET
        }
    }
}
