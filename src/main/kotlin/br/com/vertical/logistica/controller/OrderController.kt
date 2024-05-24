package br.com.vertical.logistica.controller

import br.com.vertical.logistica.entity.OrderEntity
import br.com.vertical.logistica.entity.User
import br.com.vertical.logistica.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/upload")
    fun uploadFile(@RequestBody fileContent: String): ResponseEntity<List<Map<String, Any>>> {
        return orderService.processLegacyFile(fileContent)
    }

//    @GetMapping("/{userId}")
//    fun getUserOrders(@PathVariable userId: Long): List<User> {
//        return orderService.getUserOrders(userId)
//    }
}
