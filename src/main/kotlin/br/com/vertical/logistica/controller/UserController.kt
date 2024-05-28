package br.com.vertical.logistica.controller

import br.com.vertical.logistica.dto.UserDTO
import br.com.vertical.logistica.exceptions.InvalidParamsException
import br.com.vertical.logistica.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    private val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/orders")
    fun getAllOrdersByUsers(
        @RequestParam(name = "init_date", required = false) initDate: String?,
        @RequestParam(name = "finish_date", required = false) finishDate: String?
    ): ResponseEntity<List<UserDTO>> {
        if (!userService.areDatesValid(initDate, finishDate)) {
            logger.error("Parâmetros inválidos")
            throw InvalidParamsException.Invalid
        }

        val resp = userService.getAllUsersWithOrders(initDate, finishDate)

        logger.info("Requisição concluída com sucesso")
        return ResponseEntity.ok().body(resp)
    }

    @GetMapping("/orders/{orderId}")
    fun getUserByOrderId(@PathVariable("orderId") orderId: Long): ResponseEntity<UserDTO> {
        logger.info("Recebendo requisição para consultar os pedidos")

        val resp = userService.getByOrderId(orderId)

        logger.info("Requisição concluída com sucesso")
        return ResponseEntity.ok().body(resp)
    }
}