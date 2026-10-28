package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CatAdmin
import com.example.ui.theme.CatCoffee
import com.example.ui.theme.CatIT
import com.example.ui.theme.CatMeeting
import com.example.ui.theme.CatSick
import com.example.ui.theme.CatSmoke
import com.example.ui.theme.CatToilet

enum class BreakCategory(
    val id: String,
    val titleEn: String,
    val titleDe: String,
    val subtitleEn: String,
    val subtitleDe: String,
    val emoji: String,
    val color: Color,
    val isMeetingBurner: Boolean = false,
    val wittyQuotesEn: List<String>,
    val wittyQuotesDe: List<String>
) {
    RESTROOM(
        id = "toilet",
        titleEn = "Bio Break",
        titleDe = "Klogang",
        subtitleEn = "Royal Flush & quiet contemplation",
        subtitleDe = "Das stille Örtchen / Royal Flush",
        emoji = "🚽",
        color = CatToilet,
        wittyQuotesEn = listOf(
            "Boss makes a dollar, I make a dime, that's why I pause on company time.",
            "Deep strategic reflection in ceramic isolation.",
            "Physical relief meets financial enrichment.",
            "The porcelain throne: where genuine thinking happens."
        ),
        wittyQuotesDe = listOf(
            "Boss makes a dollar, I make a dime, that's why I poop on company time.",
            "Strategische Tiefenreflexion auf Keramik.",
            "Körperliche Entlastung, finanzielle Bereicherung.",
            "Goldgräber-Stimmung im Sanitärbereich."
        )
    ),
    COFFEE(
        id = "coffee",
        titleEn = "Coffee & Watercooler",
        titleDe = "Kaffee & Flurfunk",
        subtitleEn = "Strategic caffeine networking",
        subtitleDe = "Strategisches Koffein-Networking",
        emoji = "☕",
        color = CatCoffee,
        wittyQuotesEn = listOf(
            "The office coffee machine is the true company intranet.",
            "Crucial corridor alignment before the board meeting.",
            "Refueling beans strictly on corporate expense.",
            "No coffee beans, no slide decks."
        ),
        wittyQuotesDe = listOf(
            "Die Kaffeemaschine ist das wahre Intranet.",
            "Wichtiges Abstimmen von Gerüchten der Chefetage.",
            "Koffein tanken auf Firmenkosten.",
            "Ohne Bohnen keine Leistung."
        )
    ),
    MEETING(
        id = "meeting",
        titleEn = "Pointless Meeting",
        titleDe = "Sinnlos-Meeting",
        subtitleEn = "Corporate coma & budget burner",
        subtitleDe = "Konzern-Koma & Geldverbrenner",
        emoji = "🥱",
        color = CatMeeting,
        isMeetingBurner = true,
        wittyQuotesEn = listOf(
            "This entire hour could have been a three-sentence email.",
            "Spinning in circles with high-fidelity animated charts.",
            "Actively nodding while mentally planning the weekend.",
            "Burning corporate capital one bullet point at a time."
        ),
        wittyQuotesDe = listOf(
            "Dieses Meeting hätte eine E-Mail sein können.",
            "Wir drehen uns im Kreis, aber mit schicker PowerPoint.",
            "Konzernkapital verbrennen im Minutentakt.",
            "Aktiv zuhören, innerlich den Feierabend planen."
        )
    ),
    IT_UPDATE(
        id = "it_update",
        titleEn = "IT & System Updates",
        titleDe = "IT-Streik & Updates",
        subtitleEn = "Windows update running (37% complete)",
        subtitleDe = "Windows updatet... (37% abgeschlossen)",
        emoji = "⏳",
        color = CatIT,
        wittyQuotesEn = listOf(
            "Please do not turn off your computer.",
            "The IT helpdesk has queued ticket #4092. Priority: Low.",
            "Waiting for the reboot is billable zen meditation.",
            "Hardware strike: A union-mandated chip cool-down."
        ),
        wittyQuotesDe = listOf(
            "Bitte schalten Sie den Computer nicht aus.",
            "Der IT-Helpdesk hat ein Ticket eröffnet. Priorität: Niedrig.",
            "Warten auf den Neustart ist bezahlte Kontemplation.",
            "Hardware streikt – Gewerkschaftliche Pause der Chips."
        )
    ),
    STRETCH_WALK(
        id = "smoke",
        titleEn = "Fresh Air & Stretch",
        titleDe = "Frischluft & Dehnen",
        subtitleEn = "Stepping away from the screen",
        subtitleDe = "Schreibtischpause & Frischluftzufuhr",
        emoji = "🚶‍♂️",
        color = CatSmoke,
        wittyQuotesEn = listOf(
            "Ergonomic health initiative sanctioned by gravity.",
            "Spontaneous corridor standup meeting on the go.",
            "Resting tired eyes from 8 hours of spreadsheet glare.",
            "Checking whether the outside world still exists."
        ),
        wittyQuotesDe = listOf(
            "Ergonomische Bildschirmpause für die Rückengesundheit.",
            "Frischluftzufuhr mit Blick auf den Büroparkplatz.",
            "Spontanes Stand-up-Meeting im Treppenhaus.",
            "Wichtige Beinstreckung zwischen zwei Telefonaten."
        )
    ),
    ADMIN_INBOX(
        id = "life_admin",
        titleEn = "Admin & Inbox Zero",
        titleDe = "Admin & Organisation",
        subtitleEn = "Sorting desk tasks & personal logistics",
        subtitleDe = "Private Marktrecherche & Schreibtisch-Ordnung",
        emoji = "📑",
        color = CatAdmin,
        wittyQuotesEn = listOf(
            "Booking vacation flights disguised as quarterly vendor research.",
            "Delivery courier arrives in 14 minutes. High vigilance.",
            "Sorting junk mail for supreme inbox hygiene.",
            "Multitasking: Budget report on left monitor, cart on the right."
        ),
        wittyQuotesDe = listOf(
            "Recherche nach günstigen Urlaubszielen während der Kernarbeitszeit.",
            "Der Paketbote klingelt in 12 Minuten.",
            "Wichtige persönliche Logistikoptimierung am Schreibtisch.",
            "Multitasking: Tabelle offen, Warenkorb voll."
        )
    ),
    SICK_LEAVE(
        id = "sick",
        titleEn = "Sick Day & Recovery",
        titleDe = "Krankheitstag",
        subtitleEn = "Paid sick leave & recuperation",
        subtitleDe = "Lohnfortzahlung & Gelber Schein",
        emoji = "🤒",
        color = CatSick,
        wittyQuotesEn = listOf(
            "Legally mandated recuperation: Tea, bed, and full pay.",
            "Coming to work sick endangers everyone. Recovery is duty!",
            "Recharging body and mind on corporate allowance.",
            "Netflix, ginger tea, and 100% salary intact."
        ),
        wittyQuotesDe = listOf(
            "Der gelbe Schein: Die heiligste Urkunde des deutschen Arbeitsrechts.",
            "Lohnfortzahlung im Krankheitsfall ist gesetzlich verordnete Regeneration.",
            "Genesung auf Firmenkosten: Kamillentee, Bettruhe und 100% Gehalt.",
            "Wer krank zur Arbeit kommt, gefährdet das Team. Kurieren ist Bürgerpflicht!"
        )
    );

    fun getTitle(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) titleDe else titleEn
    fun getSubtitle(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) subtitleDe else subtitleEn
    fun getQuotes(lang: AppLanguage): List<String> = if (lang == AppLanguage.GERMAN) wittyQuotesDe else wittyQuotesEn

    // Backward compatibility getters
    val title: String get() = titleEn
    val subtitle: String get() = subtitleEn
    val wittyQuotes: List<String> get() = wittyQuotesEn

    companion object {
        val TOILET: BreakCategory get() = RESTROOM

        fun fromId(id: String): BreakCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: RESTROOM
        }
    }
}
