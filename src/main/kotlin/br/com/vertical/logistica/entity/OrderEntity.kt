package br.com.vertical.logistica.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@JsonIgnoreProperties("user")
data class OrderEntity(
    @Id
    val id: Long,
    val date: LocalDate,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToMany(mappedBy = "orderEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val products: MutableList<Product> = mutableListOf()
) {
    var total: BigDecimal = BigDecimal.ZERO
    private set

    fun calculateTotal(): BigDecimal {
        total = products.fold(BigDecimal.ZERO) { acc, product ->
            acc + product.productValue
        }
        return total
    }
}