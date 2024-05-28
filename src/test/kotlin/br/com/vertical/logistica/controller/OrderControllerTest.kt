package br.com.vertical.logistica.controller

import br.com.vertical.logistica.entity.Message
import br.com.vertical.logistica.exceptions.InvalidRequestException
import br.com.vertical.logistica.service.OrderService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class OrderControllerTest {

    private lateinit var orderService: OrderService
    private lateinit var orderController: OrderController

    @BeforeEach
    fun setUp() {
        orderService = mock()
        orderController = OrderController(orderService)
    }

    @Test
    fun `inputData should process order successfully when data archive is not empty`() {
        val dataArchive = "valid data archive"
        val responseMessage = Message("Arquivo processado inteiramente com sucesso!")

        val result = orderController.inputData(dataArchive)

        verify(orderService).processOrder(dataArchive)
        assertTrue(result is ResponseEntity<*>)
        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(responseMessage, result.body)
    }

    @Test
    fun `inputData should throw InvalidRequestException when data archive is null or empty`() {
        val dataArchive = ""

        val exception = assertThrows(InvalidRequestException::class.java) {
            orderController.inputData(dataArchive)
        }
        assertEquals(InvalidRequestException.Invalid, exception)
    }
}
