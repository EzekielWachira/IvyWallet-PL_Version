package com.ivy.core.domain.action.transaction

import com.ivy.core.persistence.algorithm.accountcache.AccountCacheDao
import com.ivy.core.persistence.algorithm.accountcache.AccountCacheEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant

class AccountCacheDaoFake : AccountCacheDao {

    val accounts = MutableStateFlow(mutableListOf<AccountCacheEntity>())

    override fun findAccountCache(accountId: String): Flow<AccountCacheEntity?> {
        return accounts.map {
            it.find { it.accountId == accountId }
        }
    }

    override suspend fun findTimestampById(accountId: String): Instant? {
        return accounts.map {
            it.find { it.accountId == accountId }?.timestamp
        }.first()
    }

    override suspend fun save(cache: AccountCacheEntity) {
        val account = listOf(cache)
        accounts.value += cache
    }

    override suspend fun delete(accountId: String) {
        val account = accounts.value.find { it.accountId == accountId } ?: return
        accounts.map {
            it.remove(
                account
            )
        }
    }

    override suspend fun deleteAll() {
        accounts.value = emptyList<AccountCacheEntity>().toMutableList()
    }
}