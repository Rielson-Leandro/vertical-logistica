package br.com.vertical.logistica.service

import br.com.vertical.logistica.dto.OrderDTO
import br.com.vertical.logistica.dto.ProductDTO
import br.com.vertical.logistica.dto.UserDTO
import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.ProductEntity
import br.com.vertical.logistica.entity.UserEntity
import br.com.vertical.logistica.exceptions.OrderNotFoundException
import br.com.vertical.logistica.utils.OrderResponse.convertToOrderDTO
import br.com.vertical.logistica.repository.OrderRepository
import br.com.vertical.logistica.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
@CacheConfig(cacheNames = ["users", "orders"])
class UserService(
    private val ordersRepository: OrderRepository,
    private val usersRepository: UserRepository
    ) {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserService::class.java)
        private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @Cacheable(cacheNames = ["allUsersWithOrders"], key = "#initDate?.hashCode()?.toString()?.plus('-')?.plus(#finishDate?.hashCode()?.toString())")
    fun getAllUsersWithOrders(initDate: String?, finishDate: String?): List<UserDTO> {
        val consultedUsers = usersRepository.findAll()

        return consultedUsers.mapNotNull { user ->
            val consultedOrders = getOrdersByDateRange(initDate, finishDate, user.id)

            val ordersResponse: MutableList<OrderDTO?> = consultedOrders.mapNotNull { order ->
                val productsResponse = order.products.mapNotNull(::convertProductEntityToDTO)
                convertToOrderDTO(order, productsResponse)
            }.toMutableList()


            convertUserEntityToDTO(user, ordersResponse)
        }
    }

    @Cacheable(value = ["order"])
    fun getByOrderId(orderId: Long): UserDTO? {
        val order = ordersRepository.findById(orderId)
            .orElseThrow {
                LOGGER.error("Pedido com id '$orderId' não encontrado")
                OrderNotFoundException.OrderNotFound(orderId)
            }

        val productsResponse = order.products.mapNotNull(::convertProductEntityToDTO)
        val orderResponse = convertToOrderDTO(order, productsResponse)

        return mutableListOf(orderResponse).let { convertUserEntityToDTO(order.user, it) }
    }

    fun areDatesValid(initDate: String?, finishDate: String?): Boolean {
        return try {
            val init = LocalDate.parse(initDate, DATE_FORMATTER)
            val finish = LocalDate.parse(finishDate, DATE_FORMATTER)
            init.isBefore(finish) || init == finish
        } catch (e: Exception) {
            false
        }
    }

    private fun getOrdersByDateRange(initDate: String?, finishDate: String?, userId: Long?): List<OrderEntity> {
        return if (!initDate.isNullOrBlank() && !finishDate.isNullOrBlank()) {
            ordersRepository.findAllByDateBetween(
                LocalDate.parse(initDate, DATE_FORMATTER),
                LocalDate.parse(finishDate, DATE_FORMATTER)
            )
        } else {
            ordersRepository.findAllByUserId(userId ?: return emptyList())
        }
    }

    private fun convertUserEntityToDTO(entity: UserEntity?, orders: MutableList<OrderDTO?>): UserDTO? {
        return entity?.let {
            UserDTO(
                id = it.id ?: error("User ID cannot be null"),
                name = it.name ?: error("User name cannot be null"),
                orders = orders
            )
        }
    }

    private fun convertProductEntityToDTO(product: ProductEntity?): ProductDTO? {
        return product?.let {
            ProductDTO(
                id = it.id ?: error("ID do produto é nulo"),
                value = it.productValue?.toString() ?: error("Valor do produto é nulo")
            )
        }
    }
}