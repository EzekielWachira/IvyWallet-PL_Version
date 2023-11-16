package com.ivy.core.domain.action.exchange

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ivy.core.domain.action.settings.basecurrency.BaseCurrencyFlow
import com.ivy.core.domain.utils.MainCoroutineExtension
import com.ivy.core.domain.utils.TestDispatchers
import com.ivy.core.domain.utils.exchangeRateOverrideEntity
import com.ivy.core.domain.utils.exchangeRatesEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class ExchangeRatesFlowTest {

    private lateinit var exchangeRatesFlow: ExchangeRatesFlow
    private lateinit var baseCurrencyFlow: BaseCurrencyFlow
    private lateinit var exchangeRateDaoFake: ExchangeRateDaoFake
    private lateinit var exchangeRateOverrideDaoFake: ExchangeRateOverrideDaoFake
    private lateinit var testDispatchers: TestDispatchers

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        baseCurrencyFlow = mockk(relaxed = true)
        every { baseCurrencyFlow.invoke() } returns flowOf("", "EUR")
        exchangeRateDaoFake = ExchangeRateDaoFake()
        exchangeRateOverrideDaoFake = ExchangeRateOverrideDaoFake()
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
        exchangeRatesFlow =
            ExchangeRatesFlow(
                baseCurrencyFlow = baseCurrencyFlow,
                exchangeRateDao = exchangeRateDaoFake,
                exchangeRateOverrideDao = exchangeRateOverrideDaoFake,
                dispatchers = testDispatchers
            )

    }

    @Test
    fun `Test exchange rates flow emissions`() = runTest {
        val exchangeRates = listOf(
            exchangeRatesEntity("USD", 1.3),
            exchangeRatesEntity("CAD", 1.7),
            exchangeRatesEntity("AUD", 1.9),
        )

        val exchangeRatesOverride = listOf(
            exchangeRateOverrideEntity("CAD", 1.5)
        )

        exchangeRatesFlow().test {
            awaitItem() // Initial emission, ignore
            exchangeRateDaoFake.save(exchangeRates)
            exchangeRateOverrideDaoFake.save(exchangeRatesOverride)

            val rates1 = awaitItem()
            assertThat(rates1.rates).hasSize(3)
            assertThat(rates1.rates["USD"]).isEqualTo(1.3)
            assertThat(rates1.rates["CAD"]).isEqualTo(1.5)
            assertThat(rates1.rates["AUD"]).isEqualTo(1.9)

            exchangeRateDaoFake.save(emptyList())

            val rates2 = awaitItem()
            assertThat(rates2.rates).hasSize(1)
             assertThat(rates2.rates["USD"]).isEqualTo(1.3)
            assertThat(rates2.rates["CAD"]).isEqualTo(1.7)
            assertThat(rates2.rates["AUD"]).isEqualTo(1.9)

        }
    }
}