package br.com.vertical.logistica.utils

import java.math.BigDecimal

data class ProcessedLine(
    val userId: Long?,
    val userName: String?,
    val orderId: Long?,
    val productId: Long?,
    val productValue: BigDecimal?,
    val orderDate: String?
) {
    companion object {
        private const val LINE_LENGTH = 95
        private val USER_ID_RANGE = 0..9
        private val USER_NAME_RANGE = 10..54
        private val ORDER_ID_RANGE = 55..64
        private val PRODUCT_ID_RANGE = 65..74
        private val PRODUCT_VALUE_RANGE = 75..86
        private val ORDER_DATE_RANGE = 87..94

        fun from(line: String): ProcessedLine? {
            if (!line.isValid()) return null
            return ProcessedLine(
                userId = line.extractLong(USER_ID_RANGE),
                userName = line.extractString(USER_NAME_RANGE),
                orderId = line.extractLong(ORDER_ID_RANGE),
                productId = line.extractLong(PRODUCT_ID_RANGE),
                productValue = line.extractBigDecimal(PRODUCT_VALUE_RANGE),
                orderDate = line.extractString(ORDER_DATE_RANGE)
            )
        }

        private fun String.isValid() = this.length == LINE_LENGTH

        private fun String.extractLong(range: IntRange): Long? =
            this.substring(range).trimLeadingZeros().toLongOrNull()

        private fun String.extractString(range: IntRange): String? =
            this.substring(range).trimLeadingSpaces().takeIf { it.isNotEmpty() }

        private fun String.extractBigDecimal(range: IntRange): BigDecimal? =
            this.substring(range).trim().toBigDecimalOrNull()

        private fun String.trimLeadingZeros() = this.replaceFirst("^0+".toRegex(), "")

        private fun String.trimLeadingSpaces() = this.replaceFirst("^\\s+".toRegex(), "")
    }
}
