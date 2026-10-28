package com.example.model

data class LootEquivalent(
    val id: String,
    val name: String,
    val emoji: String,
    val cost: Double,
    val unitLabel: String,
    val funDescription: String
) {
    companion object {
        val ALL_LOOT = listOf(
            LootEquivalent(
                id = "coffee",
                name = "Barista Kaffee",
                emoji = "☕",
                cost = 4.50,
                unitLabel = "Tassen",
                funDescription = "Feinster Hafermilch-Flat-White vom Hipster-Café"
            ),
            LootEquivalent(
                id = "doner",
                name = "Döner Kebab",
                emoji = "🥙",
                cost = 8.00,
                unitLabel = "Döner",
                funDescription = "Knuspriges Fladenbrot mit Kräuter-Knoblauch-Soße"
            ),
            LootEquivalent(
                id = "pizza",
                name = "Pizza Lieferdienst",
                emoji = "🍕",
                cost = 15.00,
                unitLabel = "Pizzen",
                funDescription = "Große Steinofen-Pizza für die Mittagspause"
            ),
            LootEquivalent(
                id = "beer",
                name = "Kasten Bier",
                emoji = "🍺",
                cost = 20.00,
                unitLabel = "Kästen",
                funDescription = "Flüssiges Feierabend-Gold für das ganze Team"
            ),
            LootEquivalent(
                id = "gas",
                name = "Tankfüllung",
                emoji = "⛽",
                cost = 90.00,
                unitLabel = "Füllungen",
                funDescription = "Voller Tank für den Wochenend-Trip"
            ),
            LootEquivalent(
                id = "ps5",
                name = "PlayStation 5",
                emoji = "🎮",
                cost = 550.00,
                unitLabel = "Konsolen",
                funDescription = "Next-Gen Konsole für echte Gaming-Sessions"
            ),
            LootEquivalent(
                id = "rent",
                name = "1 Monatsmiete",
                emoji = "🏠",
                cost = 950.00,
                unitLabel = "Mieten",
                funDescription = "Warmmiete für die heimische Ruheoase"
            )
        )

        fun findNextMilestone(currentAmount: Double): MilestoneProgress {
            val nextLoot = ALL_LOOT.firstOrNull { it.cost > currentAmount } ?: ALL_LOOT.last()
            val progress = (currentAmount / nextLoot.cost).toFloat().coerceIn(0f, 1f)
            val remaining = (nextLoot.cost - currentAmount).coerceAtLeast(0.0)

            return MilestoneProgress(
                targetLoot = nextLoot,
                progress = progress,
                remainingCost = remaining,
                percent = (progress * 100).toInt()
            )
        }
    }
}

data class MilestoneProgress(
    val targetLoot: LootEquivalent,
    val progress: Float,
    val remainingCost: Double,
    val percent: Int
)
