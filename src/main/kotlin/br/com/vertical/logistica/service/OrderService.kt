package br.com.vertical.logistica.service

import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.ProductEntity
import br.com.vertical.logistica.entity.UserEntity
import br.com.vertical.logistica.utils.ProcessedLine
import br.com.vertical.logistica.repository.OrderRepository
import br.com.vertical.logistica.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class OrderService(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(OrderService::class.java)
        private const val YYYYMMDD: String = "yyyyMMdd"

    }

    @Transactional
    fun processOrder(fileString: String): List<String> {
        val (errorLines, usersToPersist, ordersToPersist) = mutableListOf<String>().let {
            Triple(it, mutableListOf<UserEntity>(), mutableListOf<OrderEntity>())
        }

        fileString.lineSequence().forEach { line ->
            val processedLine = ProcessedLine.from(line)
            if (processedLine == null) {
                errorLines.add(line)

            } else {
                processLineToEntities(processedLine, usersToPersist, ordersToPersist, errorLines)
            }
        }

        persistAndMergeOrders(usersToPersist, ordersToPersist)

        return errorLines
    }

    private fun processLineToEntities(
        processedLine: ProcessedLine,
        usersToPersist: MutableList<UserEntity>,
        ordersToPersist: MutableList<OrderEntity>,
        errorLines: MutableList<String>
    ) {
        val (userId, userName, orderId, productId, productValue, orderStrDate) = processedLine

        if (listOf(userId, userName, orderId, orderStrDate, productId, productValue).any { it == null }) {
            errorLines.add(processedLine.toString())
            return
        }

        val newUser = usersToPersist.find { it.id == userId } ?: UserEntity(userId, userName, mutableListOf()).also {
            usersToPersist.add(it)
        }
        val existingOrder = ordersToPersist.find { it.id == orderId }

        existingOrder?.products?.add(ProductEntity(productId, productValue))
            ?: run {
                val newOrder = OrderEntity(
                    orderId,
                    LocalDate.parse(orderStrDate, DateTimeFormatter.ofPattern(YYYYMMDD)),
                    newUser,
                    BigDecimal.ZERO,
                    mutableListOf()
                )
                newOrder.products.add(ProductEntity(productId, productValue))
                ordersToPersist.add(newOrder)
            }
    }

    @Transactional
    private fun persistAndMergeOrders(newUserList: List<UserEntity>, newOrderList: List<OrderEntity>) {
        val userPersistList = newUserList.filter { user ->
            userRepository.findById(user.id!!).isEmpty
        }
        userRepository.saveAll(userPersistList)

        val orderPersistList = newOrderList.map { newOrder ->
            val existingOrder = orderRepository.findById(newOrder.id!!)
            if (existingOrder.isPresent) {
                existingOrder.get().apply { products.addAll(newOrder.products) }
            } else {
                newOrder
            }
        }

        orderPersistList.forEach { order ->
            order.total = totalCalculate(order.products)
        }

        orderRepository.saveAll(orderPersistList)
    }

    private fun totalCalculate(products: List<ProductEntity>): BigDecimal {
        return products.fold(BigDecimal.ZERO) { acc, product ->
            acc + (product.productValue ?: BigDecimal.ZERO)
        }
    }
}