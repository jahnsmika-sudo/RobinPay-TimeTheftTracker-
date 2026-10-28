package com.example.model

enum class Rank(
    val titleEn: String,
    val titleDe: String,
    val emoji: String,
    val minEarnings: Double,
    val maxEarnings: Double,
    val descriptionEn: String,
    val descriptionDe: String,
    val quoteEn: String,
    val quoteDe: String
) {
    INTERN(
        titleEn = "Sneaky Intern",
        titleDe = "Praktikant auf Abwegen",
        emoji = "🐣",
        minEarnings = 0.0,
        maxEarnings = 15.0,
        descriptionEn = "Timid early bio breaks. Every single cent counts toward economic freedom.",
        descriptionDe = "Erste zaghafte Klogänge. Jeder Cent zählt auf dem Weg zur finanziellen Emanzipation.",
        quoteEn = "Small steps, fully compensated.",
        quoteDe = "Aller Anfang ist schwer, aber bezahlt."
    ),
    COFFEE_SPEC(
        titleEn = "Watercooler Strategist",
        titleDe = "Flurfunk-Spezialist",
        emoji = "☕",
        minEarnings = 15.0,
        maxEarnings = 60.0,
        descriptionEn = "Knows office gossip days before HR does. The coffee machine pays for itself.",
        descriptionDe = "Kennt jeden Büro-Klatsch noch vor dem Betriebsrat. Der Kaffeekonsum finanziert sich von selbst.",
        quoteEn = "Executive-level hallway diplomacy.",
        quoteDe = "Networking auf Vorstands-Niveau."
    ),
    QUIET_QUITTER(
        titleEn = "Quiet Quitting Manager",
        titleDe = "Quiet-Quitting-Manager",
        emoji = "🥷",
        minEarnings = 60.0,
        maxEarnings = 200.0,
        descriptionEn = "Master of nodding gently with the mic on mute. Inconspicuously present, maximally rewarded.",
        descriptionDe = "Meisterhaftes Nicken mit stummgeschaltetem Mikrofon. Unsichtbar anwesend, maximal vergütet.",
        quoteEn = "Minimal friction, maximum charisma.",
        quoteDe = "Minimaler Einsatz, maximales Charisma."
    ),
    GHOST(
        titleEn = "Corporate Ghost",
        titleDe = "Konzern-Phantom",
        emoji = "👻",
        minEarnings = 200.0,
        maxEarnings = 600.0,
        descriptionEn = "Calendar perpetually blocked with 'Focus Time'. Legendary, yet rarely spotted at their desk.",
        descriptionDe = "Im Kalender durchgehend geblockt. Legendär, aber selten an seinem Schreibtisch gesichtet.",
        quoteEn = "Whoever seeks me shall not find me.",
        quoteDe = "Wer suchet, der findet mich nicht."
    ),
    ROBIN_HOOD(
        titleEn = "Robin Hood of Capitalism",
        titleDe = "Robin Hood der Wirtschaft",
        emoji = "👑",
        minEarnings = 600.0,
        maxEarnings = 999999.0,
        descriptionEn = "The undisputed legend. Takes back what rightfully belongs to the workforce.",
        descriptionDe = "Der unangefochtene Champion. Holt sich zurück, was der Belegschaft zusteht.",
        quoteEn = "Extracts from corporations, deposits into own savings.",
        quoteDe = "Nimmt den Konzernen, gibt dem eigenen Bankkonto."
    );

    fun getTitle(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) titleDe else titleEn
    fun getDescription(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) descriptionDe else descriptionEn
    fun getQuote(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) quoteDe else quoteEn

    // Backward compatibility getters
    val title: String get() = titleEn
    val description: String get() = descriptionDe
    val quote: String get() = quoteEn

    companion object {
        fun getRankFor(earnings: Double): RankProgress {
            val validRank = entries.find { earnings < it.maxEarnings } ?: ROBIN_HOOD
            val isMax = validRank == ROBIN_HOOD

            val progress = if (isMax) {
                1.0f
            } else {
                val range = validRank.maxEarnings - validRank.minEarnings
                val earnedInRange = (earnings - validRank.minEarnings).coerceAtLeast(0.0)
                (earnedInRange / range).toFloat().coerceIn(0.0f, 1.0f)
            }

            val nextRank = if (isMax) null else entries.getOrNull(validRank.ordinal + 1)
            val remainingToNext = if (isMax) 0.0 else (validRank.maxEarnings - earnings).coerceAtLeast(0.0)

            return RankProgress(
                currentRank = validRank,
                nextRank = nextRank,
                progressToNext = progress,
                remainingAmount = remainingToNext,
                totalEarned = earnings
            )
        }
    }
}

data class RankProgress(
    val currentRank: Rank,
    val nextRank: Rank?,
    val progressToNext: Float,
    val remainingAmount: Double,
    val totalEarned: Double
)
