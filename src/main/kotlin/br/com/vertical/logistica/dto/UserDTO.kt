package br.com.vertical.logistica.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(value = ["id", "name", "orders"])
data class UserDTO(
    @JsonProperty("user_id")
    val id: Long,
    val name: String,
    val orders: MutableList<OrderDTO?>
)
