package com.ivy.core.domain.action.transaction.transfer

import com.ivy.core.domain.action.transaction.WriteTrnsAct
import com.ivy.core.domain.action.transaction.WriteTrnsBatchAct
import com.ivy.core.domain.action.transaction.account
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.TrnTime
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class WriteTransferActTest {

    private lateinit var writeTransferAct: WriteTransferAct
    private lateinit var writeTrnsBatchAct: WriteTrnsBatchAct
    private lateinit var transferByBatchIdAct: TransferByBatchIdAct
    private lateinit var writeTrnsAct: WriteTrnsAct

    @BeforeEach
    fun setUp() {

        writeTrnsBatchAct = mockk(relaxed = true)
        transferByBatchIdAct = mockk(relaxed = true)
        writeTrnsAct = mockk(relaxed = true)

        writeTransferAct = WriteTransferAct(
            writeTrnsBatchAct, transferByBatchIdAct, writeTrnsAct
        )

    }

    @Test
    fun `Add transfer, fees are considered`() = runBlocking {
        writeTransferAct(
            ModifyTransfer.add(
                data = TransferData(
                    amountFrom = Value(50.0, "EUR"),
                    amountTo = Value(60.0, "USD"),
                    accountFrom = account().copy(
                        name = "Test Account 1"
                    ),
                    accountTo = account().copy(
                        name = "Test Account 2",
                    ),
                    category = null,
                    time = TrnTime.Actual(LocalDateTime.now()),
                    title =  "Test transfer",
                    description = "Test description",
                    fee = Value(5.0, "EUR"),
                    sync = Sync(
                        state = SyncState.Syncing,
                        lastUpdated = LocalDateTime.now()
                    )
                )
            )
        )

        coVerify {
            writeTrnsBatchAct(
                match {
                    it as WriteTrnsBatchAct.ModifyBatch.Save

                    val from  = it.batch.trns[0]
                    val to  = it.batch.trns[1]
                    val fee  = it.batch.trns[2]

                    from.value.amount == 50.0 &&
                            to.value.amount == 60.0 &&
                            fee.value.amount == 5.0&&
                            fee.type == TransactionType.Expense
                }
            )
        }
    }
}