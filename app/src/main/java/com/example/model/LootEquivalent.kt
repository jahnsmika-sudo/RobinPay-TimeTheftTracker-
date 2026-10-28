package com.example.model

data class LootEquivalent(
    val id: String,
    val nameEn: String,
    val nameDe: String,
    val emoji: String,
    val cost: Double,
    val unitLabelEn: String,
    val unitLabelDe: String,
    val funDescriptionEn: String,
    val funDescriptionDe: String
) {
    fun getName(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) nameDe else nameEn
    fun getUnitLabel(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) unitLabelDe else unitLabelEn
    fun getFunDescription(lang: AppLanguage): String = if (lang == AppLanguage.GERMAN) funDescriptionDe else funDescriptionEn

    // Backward compatibility
    val name: String get() = nameEn
    val unitLabel: String get() = unitLabelEn
    val funDescription: String get() = funDescriptionEn

    companion object {
        val ALL_LOOT = listOf(
            LootEquivalent(
                id = "coffee",
                nameEn = "Barista Latte",
                nameDe = "Barista Kaffee",
                emoji = "☕",
                cost = 4.50,
                unitLabelEn = "Cups",
                unitLabelDe = "Tassen",
                funDescriptionEn = "Silky oat flat white from the local hipster café",
                funDescriptionDe = "Feinster Hafermilch-Flat-White vom Hipster-Café"
            ),
            LootEquivalent(
                id = "doner",
                nameEn = "Street Food Kebab",
                nameDe = "Döner Kebab",
                emoji = "🥙",
                cost = 8.00,
                unitLabelEn = "Kebabs",
                unitLabelDe = "Döner",
                funDescriptionEn = "Warm crispy flatbread with extra garlic sauce",
                funDescriptionDe = "Knuspriges Fladenbrot mit Kräuter-Knoblauch-Soße"
            ),
            LootEquivalent(
                id = "pizza",
                nameEn = "Takeout Pizza",
                nameDe = "Pizza Lieferdienst",
                emoji = "🍕",
                cost = 15.00,
                unitLabelEn = "Pizzas",
                unitLabelDe = "Pizzen",
                funDescriptionEn = "Large stone-baked pizza delivered straight to your door",
                funDescriptionDe = "Große Steinofen-Pizza für die Mittagspause"
            ),
            LootEquivalent(
                id = "beer",
                nameEn = "Case of Craft Beer",
                nameDe = "Kasten Bier",
                emoji = "🍺",
                cost = 20.00,
                unitLabelEn = "Cases",
                unitLabelDe = "Kästen",
                funDescriptionEn = "Liquid gold for after-work team celebrations",
                funDescriptionDe = "Flüssiges Feierabend-Gold für das ganze Team"
            ),
            LootEquivalent(
                id = "gas",
                nameEn = "Full Tank of Gas",
                nameDe = "Tankfüllung",
                emoji = "⛽",
                cost = 90.00,
                unitLabelEn = "Tanks",
                unitLabelDe = "Füllungen",
                funDescriptionEn = "Full tank for a spontaneous weekend road trip",
                funDescriptionDe = "Voller Tank für den Wochenend-Trip"
            ),
            LootEquivalent(
                id = "ps5",
                nameEn = "PlayStation 5 Console",
                nameDe = "PlayStation 5",
                emoji = "🎮",
                cost = 550.00,
                unitLabelEn = "Consoles",
                unitLabelDe = "Konsolen",
                funDescriptionEn = "Next-gen gaming machine for peak weekend unwinding",
                funDescriptionDe = "Next-Gen Konsole für echte Gaming-Sessions"
            ),
            LootEquivalent(
                id = "rent",
                nameEn = "1 Month Apartment Rent",
                nameDe = "1 Monatsmiete",
                emoji = "🏠",
                cost = 950.00,
                unitLabelEn = "Months",
                unitLabelDe = "Mieten",
                funDescriptionEn = "Warm rent for your personal home sanctuary",
                funDescriptionDe = "Warmmiete für die heimische Ruheoase"
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
