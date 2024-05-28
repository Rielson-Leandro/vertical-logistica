package br.com.vertical.logistica.controller

import br.com.vertical.logistica.dto.OrderDTO
import br.com.vertical.logistica.dto.ProductDTO
import br.com.vertical.logistica.dto.UserDTO
import br.com.vertical.logistica.exceptions.InvalidParamsException
import br.com.vertical.logistica.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus

class UserControllerTest {

    private lateinit var userService: UserService
    private lateinit var userController: UserController

    @BeforeEach
    fun setUp() {
        userService = mock()
        userController = UserController(userService)
    }

    @Test
    fun `getAllOrdersByUsers should return list of users with orders when parameters are valid`() {
        val initDate = "2023-01-01"
        val finishDate = "2023-12-31"
        val usersWithOrders = listOf(UserDTO(1, "John Doe", listOf(OrderDTO(1, "100.00", "2023-01-01", listOf(ProductDTO(1, "50.00"))))))

        whenever(userService.areDatesValid(initDate, finishDate)).thenReturn(true)
        whenever(userService.getAllUsersWithOrders(initDate, finishDate)).thenReturn(usersWithOrders)

        val result = userController.getAllOrdersByUsers(initDate, finishDate)

        verify(userService).getAllUsersWithOrders(initDate, finishDate)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(usersWithOrders, result.body)
    }

    @Test
    fun `getAllOrdersByUsers should throw InvalidParamsException when parameters are invalid`() {
        val initDate = "invalid"
        val finishDate = "invalid"

        whenever(userService.areDatesValid(initDate, finishDate)).thenReturn(false)

        val exception = assertThrows(InvalidParamsException::class.java) {
            userController.getAllOrdersByUsers(initDate, finishDate)
        }
        assertEquals(InvalidParamsException.Invalid, exception)
    }

    @Test
    fun `getUserByOrderId should return user by order ID`() {
        val orderId = 1L
        val user = UserDTO(1, "John Doe", listOf(OrderDTO(1, "100.00", "2023-01-01", listOf(ProductDTO(1, "50.00")))))

        whenever(userService.getByOrderId(orderId)).thenReturn(user)

        val result = userController.getUserByOrderId(orderId)

        verify(userService).getByOrderId(orderId)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(user, result.body)
    }
}
