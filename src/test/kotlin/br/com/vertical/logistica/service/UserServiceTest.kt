package br.com.vertical.logistica.service

import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.ProductEntity
import br.com.vertical.logistica.entity.UserEntity
import br.com.vertical.logistica.exceptions.OrderNotFoundException
import br.com.vertical.logistica.repository.OrderRepository
import br.com.vertical.logistica.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.anyOrNull
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    private lateinit var orderEntity: OrderEntity
    private lateinit var userEntity: UserEntity
    private lateinit var productEntity: ProductEntity

    @BeforeEach
    fun setUp() {
        productEntity = ProductEntity(id = 1, productValue = 100.0.toBigDecimal())
        orderEntity = OrderEntity(
            id = 1,
            date = LocalDate.now(),
            user = UserEntity(id = 1, name = "Test User"),
            total = 100.0.toBigDecimal(),
            products = mutableListOf(productEntity)
        )
        userEntity = UserEntity(id = 1, name = "Test User", orderEntities = mutableListOf(orderEntity))
    }

    @Test
    fun `getAllUsersWithOrders should return users with orders between dates`() {
        val initDate = "2023-01-01"
        val finishDate = "2023-12-31"
        val orders = listOf(orderEntity)

        `when`(userRepository.findAll()).thenReturn(listOf(userEntity))
        `when`(orderRepository.findAllByDateBetween(anyOrNull(), anyOrNull())).thenReturn(orders)

        val result = userService.getAllUsersWithOrders(initDate, finishDate)

        assertEquals(1, result.size)
        assertEquals("Test User", result[0].name)
    }

    @Test
    fun `getAllUsersWithOrders should return users with all orders when no dates provided`() {
        val orders = listOf(orderEntity)

        `when`(userRepository.findAll()).thenReturn(listOf(userEntity))
        `when`(orderRepository.findAllByUserId(userEntity.id!!)).thenReturn(orders)

        val result = userService.getAllUsersWithOrders(null, null)

        assertEquals(1, result.size)
        assertEquals("Test User", result[0].name)
    }

    @Test
    fun `getByOrderId should return user with specific order`() {
        `when`(orderRepository.findById(orderEntity.id!!)).thenReturn(Optional.of(orderEntity))

        val result = userService.getByOrderId(orderEntity.id!!)

        assertNotNull(result)
        assertEquals("Test User", result?.name)
    }

    @Test
    fun `getByOrderId should throw OrderNotFoundException if order not found`() {
        `when`(orderRepository.findById(orderEntity.id!!)).thenReturn(Optional.empty())

        assertThrows(OrderNotFoundException::class.java) {
            userService.getByOrderId(orderEntity.id!!)
        }
    }

    @Test
    fun `DateValid should return false if one date is null`() {
        assertFalse(userService.areDatesValid("2023-01-01", null))
        assertFalse(userService.areDatesValid(null, "2023-12-31"))
    }

    @Test
    fun `DateValid should return false if dates are not 10 characters`() {
        assertFalse(userService.areDatesValid("2023-1-01", "2023-12-31"))
        assertFalse(userService.areDatesValid("2023-01-01", "2023-12-3"))
    }

    @Test
    fun `DateValid should return false if initDate is after finishDate`() {
        assertFalse(userService.areDatesValid("2023-12-31", "2023-01-01"))
    }

    @Test
    fun `DateValid should return true if dates are valid`() {
        assertTrue(userService.areDatesValid("2023-01-01", "2023-12-31"))
    }
}
