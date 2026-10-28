package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SalaryConfig
import com.example.model.LootEquivalent
import com.example.model.Rank
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RobinPay", appName)
    }

    @Test
    fun `salary calculation formula verifies hourly and second rates`() {
        val config = SalaryConfig(monthlyGrossSalary = 3800.0, weeklyHours = 40.0)
        val expectedMonthlyHours = 40.0 * 4.3333
        assertEquals(expectedMonthlyHours, config.monthlyHours, 0.001)

        val expectedHourly = 3800.0 / expectedMonthlyHours
        assertEquals(expectedHourly, config.hourlyRate, 0.01)

        val expectedSecondRate = expectedHourly / 3600.0
        assertEquals(expectedSecondRate, config.secondRate, 0.0001)
    }

    @Test
    fun `rank progression assigns correct satire titles based on earnings`() {
        val internProgress = Rank.getRankFor(10.0)
        assertEquals(Rank.INTERN, internProgress.currentRank)
        assertEquals(Rank.COFFEE_SPEC, internProgress.nextRank)

        val coffeeProgress = Rank.getRankFor(45.0)
        assertEquals(Rank.COFFEE_SPEC, coffeeProgress.currentRank)

        val quietQuitterProgress = Rank.getRankFor(120.0)
        assertEquals(Rank.QUIET_QUITTER, quietQuitterProgress.currentRank)

        val ghostProgress = Rank.getRankFor(400.0)
        assertEquals(Rank.GHOST, ghostProgress.currentRank)

        val robinHoodProgress = Rank.getRankFor(850.0)
        assertEquals(Rank.ROBIN_HOOD, robinHoodProgress.currentRank)
    }

    @Test
    fun `sick leave category is present and accessible`() {
        val sick = com.example.model.BreakCategory.fromId("sick")
        assertEquals(com.example.model.BreakCategory.SICK_LEAVE, sick)
        assertEquals("Krankheitstag", sick.titleDe)
        assertEquals("Sick Day & Recovery", sick.titleEn)
        assertTrue(sick.wittyQuotesEn.isNotEmpty())
        assertTrue(sick.wittyQuotesDe.isNotEmpty())
    }
}
