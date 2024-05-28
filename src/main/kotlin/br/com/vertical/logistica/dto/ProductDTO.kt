package br.com.vertical.logistica.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(value = ["id", "value"])
data class ProductDTO(
    @JsonProperty("product_id")
    val id: Long,
    val value: String
)
