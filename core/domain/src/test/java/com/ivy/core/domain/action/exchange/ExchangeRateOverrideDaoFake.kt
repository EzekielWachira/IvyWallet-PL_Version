package com.ivy.core.domain.action.exchange

import com.ivy.core.persistence.dao.exchange.ExchangeRateOverrideDao
import com.ivy.core.persistence.entity.exchange.ExchangeRateOverrideEntity
import com.ivy.data.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ExchangeRateOverrideDaoFake: ExchangeRateOverrideDao {

    private var exchangeRateEntities = MutableStateFlow<List<ExchangeRateOverrideEntity>>(emptyList())

    override suspend fun save(values: List<ExchangeRateOverrideEntity>) {
       exchangeRateEntities.value = values
    }

    override suspend fun findAllBlocking(): List<ExchangeRateOverrideEntity> {
        return exchangeRateEntities.value
    }

    override fun findAllByBaseCurrency(baseCurrency: String): Flow<List<ExchangeRateOverrideEntity>> {
        return exchangeRateEntities.map {
            it.filter {
                it.baseCurrency.uppercase() == baseCurrency.uppercase()
            }
        }
    }

    override suspend fun deleteByBaseCurrencyAndCurrency(baseCurrency: String, currency: String) {
        exchangeRateEntities.update {
            it.filter { ovveride->
                ovveride.baseCurrency != baseCurrency && ovveride.currency != currency
            }
        }
    }
}