package br.com.vertical.logistica.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Product(
    @Id
    val id: Long,
    val productValue: BigDecimal,
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    val orderEntity: OrderEntity
)
