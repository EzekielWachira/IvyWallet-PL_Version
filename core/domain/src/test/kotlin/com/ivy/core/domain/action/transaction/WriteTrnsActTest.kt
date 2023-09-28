package com.ivy.core.domain.action.transaction

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.ivy.core.domain.algorithm.accountcache.InvalidateAccCacheAct
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import com.ivy.data.attachment.Attachment
import com.ivy.data.attachment.AttachmentSource
import com.ivy.data.attachment.AttachmentType
import com.ivy.data.tag.Tag
import com.ivy.data.tag.TagState
import com.ivy.data.transaction.Transaction
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.TrnMetadata
import com.ivy.data.transaction.TrnState
import com.ivy.data.transaction.TrnTime
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class WriteTrnsActTest {

    private lateinit var accountCacheDaoFake: AccountCacheDaoFake
    private lateinit var timeProviderFake: TimeProviderFake
    private lateinit var transactionDaoFake: TransactionDaoFake
    private lateinit var writeTrnsAct: WriteTrnsAct
    private lateinit var trnsSignal: TrnsSignal
    private lateinit var invalidateAccCacheAct: InvalidateAccCacheAct

    @BeforeEach
    fun setUp() {
        accountCacheDaoFake = AccountCacheDaoFake()
        transactionDaoFake = TransactionDaoFake()
        timeProviderFake = TimeProviderFake()
        trnsSignal = TrnsSignal()
        invalidateAccCacheAct = InvalidateAccCacheAct(
            accountCacheDao = accountCacheDaoFake,
            timeProvider = timeProviderFake
        )
        writeTrnsAct = WriteTrnsAct(
            transactionDao = transactionDaoFake,
            trnsSignal = trnsSignal,
            timeProvider = timeProviderFake,
            invalidateAccCacheAct = invalidateAccCacheAct,
            accountCacheDao = accountCacheDaoFake
        )
    }

    @Test
    fun `Test save invalid data, returns None`() = runBlocking<Unit> {
        val transactionId = UUID.randomUUID()
        val tagId = UUID.randomUUID().toString()
        val tag = tag()
        val account = account()
        val attachmentId = UUID.randomUUID().toString()
        val transaction = transaction(account).copy(
            id = transactionId,
            tags = listOf(tag),
            attachments = listOf(attachment(transactionId.toString()))
        )


        writeTrnsAct(WriteTrnsAct.Input.CreateNew(transaction))

        val transactions = transactionDaoFake.findAllBlocking()

        val cachedTransaction = transactions.find { it.id == transactionId.toString() }
        val cachedTags = transactionDaoFake.tags.find { it.tagId == tagId }
        val cachedAttachment = transactionDaoFake.attachments.find { it.id == attachmentId }

        assertThat(transactions).isNotEmpty()
//        assertThat(transactions.size).isEqualTo(2)
        assertThat(cachedTransaction).isNotNull()
        assertThat(cachedTransaction?.type).isEqualTo(TransactionType.Expense)
        assertThat(cachedTags).isNotNull()
//        assertThat(cachedAttachment).isNotNull()
    }


}