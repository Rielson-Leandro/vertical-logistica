package br.com.vertical.logistica.service

import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.ProductEntity
import br.com.vertical.logistica.entity.UserEntity
import br.com.vertical.logistica.repository.OrderRepository
import br.com.vertical.logistica.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var orderRepository: OrderRepository

    @InjectMocks
    lateinit var orderService: OrderService

    @Captor
    lateinit var userCaptor: ArgumentCaptor<List<UserEntity>>

    @Captor
    lateinit var orderCaptor: ArgumentCaptor<List<OrderEntity>>

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `should process valid order lines successfully`() {
        val validLine = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n" +
                "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308"

        val result = orderService.processOrder(validLine)

        verify(userRepository, times(1)).saveAll(userCaptor.capture())
        verify(orderRepository, times(1)).saveAll(orderCaptor.capture())

        assertTrue(result.isEmpty())
   }

    @Test
    fun `should return error lines for invalid lines`() {
        val invalidLine = "InvalidLine"

        val result = orderService.processOrder(invalidLine)

        verify(userRepository, times(1)).saveAll(userCaptor.capture())
        verify(orderRepository, times(1)).saveAll(orderCaptor.capture())

        assertEquals(1, result.size)
        assertEquals(invalidLine, result[0])
    }

    @Test
    fun `should merge products for existing orders`() {
        val validLine = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n" +
                "0000000070                              Palmer Prosacco00000007530000000004     1836.7420210308"

        val existingOrder = OrderEntity(
            id = 753L,
            date = LocalDate.parse("2021-03-08"),
            user = UserEntity(id = 70L, name = "Palmer Prosacco"),
            total = BigDecimal("1836.74"),
            products = mutableListOf(ProductEntity(id = 3L, productValue = BigDecimal("1836.74")))
        )

        `when`(orderRepository.findById(753L)).thenReturn(Optional.of(existingOrder))

        val result = orderService.processOrder(validLine)

        verify(userRepository, times(1)).saveAll(userCaptor.capture())
        verify(orderRepository, times(1)).saveAll(orderCaptor.capture())

        assertTrue(result.isEmpty())
        assertEquals(1, userCaptor.value.size)
        assertEquals(1, orderCaptor.value.size)
    }

    @Test
    fun `should correctly calculate order total`() {
        val validLine = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n" +
                "0000000070                              Palmer Prosacco00000007530000000004     1836.7420210308"


        val result = orderService.processOrder(validLine)

        verify(orderRepository, times(1)).saveAll(orderCaptor.capture())

        val savedOrder = orderCaptor.value[0]
        assertEquals(BigDecimal("3673.48"), savedOrder.total)
    }

    @Test
    fun `should persist new users and orders`() {
        val validLine = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n" +
                "0000000070                              Palmer Prosacco00000007530000000004     1836.7420210308"


        val result = orderService.processOrder(validLine)

        verify(userRepository, times(1)).saveAll(userCaptor.capture())
        verify(orderRepository, times(1)).saveAll(orderCaptor.capture())

        assertTrue(result.isEmpty())
        assertEquals(1, userCaptor.value.size)
        assertEquals(1, orderCaptor.value.size)
    }
}
