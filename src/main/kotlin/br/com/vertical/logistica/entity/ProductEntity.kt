package br.com.vertical.logistica.entity

import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

@Embeddable
data class ProductEntity(
    @field:NotNull
    val id: Long? = null,
    @field:NotNull
    var productValue: BigDecimal? = null
)
