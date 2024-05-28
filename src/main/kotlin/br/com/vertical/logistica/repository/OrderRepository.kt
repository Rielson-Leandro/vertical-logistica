package br.com.vertical.logistica.repository

import br.com.vertical.logistica.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findAllByUserId(id: Long): List<OrderEntity>

    fun findAllByDateBetween(init: LocalDate, finish: LocalDate): List<OrderEntity>

}