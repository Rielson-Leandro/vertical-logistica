package br.com.vertical.logistica.service

import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.Product
import br.com.vertical.logistica.entity.User
import br.com.vertical.logistica.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class OrderService(private val userRepository: UserRepository) {

    fun processLegacyFile(fileContent: String): ResponseEntity<List<Map<String, Any>>> {
        val ordersByUser = mutableMapOf<Long, MutableMap<String, Any>>()

        fileContent.lines().forEach { line ->
            val order = processLine(line)
            if (order != null) {
                val user = order.user
                val userId = user.id

                val userMap = ordersByUser.getOrPut(userId) {
                    mutableMapOf(
                        "user" to user,
                        "orders" to mutableListOf<Map<String, Any>>()
                    )
                }
                val ordersList = userMap["orders"] as MutableList<Map<String, Any>>

                val orderMap = mutableMapOf<String, Any>(
                    "order_id" to order.id,
                    "total" to order.total.toString(),
                    "date" to order.date.toString(),
                    "products" to order.products.map { product ->
                        mapOf(
                            "product_id" to product.id,
                            "value" to product.productValue.toString()
                        )
                    }
                )
                ordersList.add(orderMap)
            }
        }

        val usersToSave = ordersByUser.values.map { it["user"] as User }
        userRepository.saveAll(usersToSave)

        val responseList = ordersByUser.values.map { it }
        return ResponseEntity.ok(responseList.toList())
    }

    private fun processLine(line: String?): OrderEntity? {
        if (line == null || line.isEmpty()) return null

        return try {
            val userId = line.substring(0, 10).trim().toLong()
            val userName = line.substring(10, 55).trim()
            val orderId = line.substring(55, 65).trim().toLong()
            val productId = line.substring(65, 75).trim().toLong()
            val value = line.substring(75, 87).trim().toBigDecimal()
            val date = LocalDate.parse(line.substring(87, 95).trim(), DateTimeFormatter.ofPattern("yyyyMMdd"))

            val user = userRepository.findById(userId).orElseGet {
                User(id = userId, name = userName)
            }

            var order = user.orderEntities.find { it.id == orderId }
            if (order != null) {
                val existingProduct = order.products.find { it.id == productId }
                if (existingProduct != null) {
                    // logger.warn("Duplicate product ($productId) found in order (${order.id}) for user ($userId)")
                } else {
                    val product = Product(id = productId, productValue = value, orderEntity = order)
                    order.products.add(product)
                }
            } else {
                order = OrderEntity(
                    id = orderId,
                    user = user,
                    date = date
                )
                val product = Product(id = productId, productValue = value, orderEntity = order)
                order.products.add(product)
                user.orderEntities.add(order)
            }
            order.calculateTotal()
            order
        } catch (e: Exception) {
            // logger.error("Error parsing line: $line, ${e.message}")
            null
        }
    }
}
