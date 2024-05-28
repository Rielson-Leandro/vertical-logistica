package br.com.vertical.logistica.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(value = ["id", "total", "date", "products"])
data class OrderDTO(
    @JsonProperty("order_id")
    val id: Long,
    val total: String,
    val date: String,
    val products: List<ProductDTO>
)
