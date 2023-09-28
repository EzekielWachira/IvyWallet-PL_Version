package com.ivy.core.domain.action.transaction

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
import java.time.LocalDateTime
import java.util.UUID


fun account() = Account(
    id = UUID.randomUUID(),
    name = "Test Transaction",
    currency = "USD",
    color = 0x00f152,
    excluded = false,
    folderId = UUID.randomUUID(),
    orderNum = 1.1,
    state = AccountState.Default,
    sync = Sync(SyncState.Synced, LocalDateTime.now()),
    icon = "123"
)

fun tag() = Tag(
    id = UUID.randomUUID().toString(),
    color = 0x00f152,
    name = "Test Tag",
    orderNum = 1.0,
    state = TagState.Default,
    sync = Sync(SyncState.Synced, LocalDateTime.now())
)


fun attachment(transactionId: String) = Attachment(
    id = UUID.randomUUID().toString(),
    associatedId = transactionId,
    uri = "Test",
    source = AttachmentSource.Local,
    filename = "Test file",
    type = AttachmentType.PDF,
    sync = Sync(SyncState.Synced, LocalDateTime.now())
)

fun transaction(account: Account) = Transaction(
    id = UUID.randomUUID(),
    account = account,
    type = TransactionType.Expense,
    value = Value(amount = 12.0, currency = "USD"),
    category = null,
    time = TrnTime.Actual(LocalDateTime.now()),
    title = "Sample title",
    description = "Sample description",
    state = TrnState.Default,
    purpose = null,
    tags = listOf(
    ),
    attachments = listOf(
    ),
    metadata = TrnMetadata(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
    sync = Sync(SyncState.Synced, LocalDateTime.now())
)
