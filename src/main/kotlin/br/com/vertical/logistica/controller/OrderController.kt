package br.com.vertical.logistica.controller

import br.com.vertical.logistica.entity.Message
import br.com.vertical.logistica.exceptions.InvalidRequestException
import br.com.vertical.logistica.service.OrderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    private val logger: Logger = LoggerFactory.getLogger(OrderController::class.java)

    @PostMapping
    fun inputData(@RequestBody fileString: String): ResponseEntity<Message> {
        val file = fileString ?: throw InvalidRequestException.Invalid
        val resp = orderService.processOrder(file)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            Message("Pedidos processados com sucesso.")
        )
    }
}
