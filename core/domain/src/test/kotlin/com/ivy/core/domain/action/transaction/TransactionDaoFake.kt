package com.ivy.core.domain.action.transaction

import androidx.sqlite.db.SupportSQLiteQuery
import com.ivy.core.persistence.dao.trn.AccountIdAndTrnTime
import com.ivy.core.persistence.dao.trn.TransactionDao
import com.ivy.core.persistence.entity.attachment.AttachmentEntity
import com.ivy.core.persistence.entity.trn.TransactionEntity
import com.ivy.core.persistence.entity.trn.TrnMetadataEntity
import com.ivy.core.persistence.entity.trn.TrnTagEntity
import com.ivy.data.SyncState

class TransactionDaoFake : TransactionDao() {

    private val transactions: MutableList<TransactionEntity> =
        mutableListOf()
    val trans: MutableList<TransactionEntity> = mutableListOf()

     val tags: MutableList<TrnTagEntity> = mutableListOf()
     val attachments = mutableListOf<AttachmentEntity>()
     val metadata = mutableListOf<TrnMetadataEntity>()

    override suspend fun saveTrnEntity(entity: TransactionEntity) {
        trans.add(entity)

    }

    override suspend fun updateTrnTagsSyncByTrnId(trnId: String, sync: SyncState) {
        trans.find { it.id == trnId }?.let {
            trans[trans.indexOf(trans.find { it.id == trnId })] =
                it.copy(
                    sync = sync
                )
        }

    }

    override suspend fun saveTags(entity: List<TrnTagEntity>) {
        entity.forEach { en ->
            tags.add(en)
        }
    }

    override suspend fun updateAttachmentsSyncByAssociatedId(
        associatedId: String,
        sync: SyncState
    ) {
        attachments.find { it.associatedId == associatedId }?.let {
            attachments[attachments.indexOf(attachments.find { it.associatedId == associatedId })] =
                it.copy(sync = sync)
        }

    }

    override suspend fun saveAttachments(entity: List<AttachmentEntity>) {
        entity.forEach { attachment ->
            attachments.add(attachment)
        }
    }

    override suspend fun updateMetadataSyncByTrnId(trnId: String, sync: SyncState) {
        metadata.find { it.trnId == trnId }?.let {
            metadata[metadata.indexOf(metadata.find { it.trnId == trnId })] =
                it.copy(sync = sync)
        }

    }

    override suspend fun saveMetadata(entity: List<TrnMetadataEntity>) {
        entity.forEach { data ->
            metadata.add(data)
        }
    }

    override suspend fun findAllBlocking(): List<TransactionEntity> {
        return trans
    }

    override suspend fun findBySQL(query: SupportSQLiteQuery): List<TransactionEntity> {
        return trans
    }

    override suspend fun findAccountIdAndTimeById(trnId: String): AccountIdAndTrnTime? {

        return trans.find { it.id == trnId }?.let {
            AccountIdAndTrnTime(accountId = it.accountId, time = it.time, timeType = it.timeType)
        }


    }

    override suspend fun updateTrnEntitySyncById(trnId: String, sync: SyncState) {
        trans.find { it.id == trnId }?.let {
            trans[trans.indexOf(trans.find { it.id == trnId })] =
                it.copy(sync = sync)
        }

    }


}