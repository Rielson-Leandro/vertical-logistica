package br.com.vertical.logistica.helpers

import java.math.BigDecimal

object ProcessDataHelper {

    fun isLineIsValid(line: String): Boolean {
        return line.length == 95
    }

    fun getUserIdAndName(line: String): Long? {
        val userId = line.substring(0, 10)
        val cleaned = cleanZeroBefore(userId)
        return cleaned?.toLongOrNull()
    }

    fun getUserNameAndId(line: String): String? {
        val userName = line.substring(10, 55)
        return cleanWhiteSpaceBefore(userName)
    }

    fun getOrderIdAndDate(line: String): Long? {
        val orderId = line.substring(55, 65)
        val cleaned = cleanZeroBefore(orderId)
        return cleaned?.toLongOrNull()
    }

    fun getProductIdAndValue(line: String): Long? {
        val productId = line.substring(65, 75)
        val cleaned = cleanZeroBefore(productId)
        return cleaned?.toLongOrNull()
    }

    fun getProductValueAndId(line: String): BigDecimal? {
        val productValue = line.substring(75, 87).trim()
        return productValue.toBigDecimalOrNull()
    }

    fun getOrderDateAndId(line: String): String? {
        val orderDate = line.substring(87, 95)
        return cleanZeroBefore(orderDate)
    }

    private fun cleanZeroBefore(str: String): String? {
        val cleanStr = str.replaceFirst("^[0\\s]+".toRegex(), "")
        return if (cleanStr.isBlank()) null else cleanStr
    }

    private fun cleanWhiteSpaceBefore(str: String): String? {
        val cleanStr = str.replaceFirst("^['\\s]+".toRegex(), "")
        return if (cleanStr.isBlank()) null else cleanStr
    }
}