package com.example.model

enum class Rank(
    val title: String,
    val emoji: String,
    val minEarnings: Double,
    val maxEarnings: Double,
    val description: String,
    val quote: String
) {
    INTERN(
        title = "Praktikant auf Abwegen",
        emoji = "🐣",
        minEarnings = 0.0,
        maxEarnings = 15.0,
        description = "Erste zaghafte Klogänge. Jeder Cent zählt auf dem Weg zur finanziellen Emanzipation.",
        quote = "Aller Anfang ist schwer, aber bezahlt."
    ),
    COFFEE_SPEC(
        title = "Flurfunk-Spezialist",
        emoji = "☕",
        minEarnings = 15.0,
        maxEarnings = 60.0,
        description = "Kennt jeden Büro-Klatsch noch vor dem Betriebsrat. Der Kaffeekonsum finanziert sich von selbst.",
        quote = "Networking auf Vorstands-Niveau."
    ),
    QUIET_QUITTER(
        title = "Quiet-Quitting-Manager",
        emoji = "🥷",
        minEarnings = 60.0,
        maxEarnings = 200.0,
        description = "Meisterhaftes Nicken mit stummgeschaltetem Mikrofon. Unsichtbar anwesend, maximal vergütet.",
        quote = "Minimaler Einsatz, maximales Charisma."
    ),
    GHOST(
        title = "Konzern-Phantom",
        emoji = "👻",
        minEarnings = 200.0,
        maxEarnings = 600.0,
        description = "Im Kalender durchgehend geblockt. Legendär, aber selten an seinem Schreibtisch gesichtet.",
        quote = "Wer suchet, der findet mich nicht."
    ),
    ROBIN_HOOD(
        title = "Robin Hood der Wirtschaft",
        emoji = "👑",
        minEarnings = 600.0,
        maxEarnings = 999999.0,
        description = "Der unangefochtene Champion. Holt sich zurück, was der Belegschaft zusteht.",
        quote = "Nimmt den Konzernen, gibt dem eigenen Bankkonto."
    );

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
