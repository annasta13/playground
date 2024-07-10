package com.annas.playground.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.annas.playground.data.domain.model.Transaction
import com.annas.playground.utils.LongExtension.toDateString

@Entity("transaction")
data class TransactionEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) {
    companion object {
        const val IN = "IN"
        const val OUT = "OUT"
    }
}

fun List<TransactionEntity>.asTransactionDomain(): List<Transaction> {
    return this.map {
        it.run {
            Transaction(
                id = id,
                type = type,
                date = timestamp.toDateString(),
                amount = amount
            )
        }
    }
}
