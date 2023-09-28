package com.ivy.core.domain.algorithm.calc

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.persistence.algorithm.calc.CalcTrn
import com.ivy.data.transaction.TransactionType
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawStatsKtTest {


    @Test
    fun `testing returns`() {

        val tenSecondsAgo = Instant.now().minusSeconds(10)
        val fiveSecondsAgo = Instant.now().minusSeconds(5)
        val threeMinutesAgo = Instant.now().minusSeconds(3)

        val stats = rawStats(
            listOf(
                CalcTrn(
                    amount = 10.0,
                    currency = "USD",
                    type = TransactionType.Income,
                    time = tenSecondsAgo
                ),
                CalcTrn(
                    amount = 5.0,
                    currency = "EUR",
                    type = TransactionType.Expense,
                    time = fiveSecondsAgo
                ),
                CalcTrn(
                    amount = 3.0,
                    currency = "USD",
                    type = TransactionType.Expense,
                    time = threeMinutesAgo
                ),
            )
        )

        assertThat(stats.expensesCount).isEqualTo(2)
        assertThat(stats.newestTrnTime).isEqualTo(threeMinutesAgo)
        assertThat(stats.expenses).isEqualTo(mapOf("EUR" to 5.0, "USD" to 3.0 ))

        assertThat(stats.incomesCount).isEqualTo(1)
        assertThat(stats.incomes).isEqualTo(mapOf("USD" to 10.0))


    }

}