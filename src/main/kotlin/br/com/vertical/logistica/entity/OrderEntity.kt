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
    fun calculateTotal(): BigDecimal {
        var total = BigDecimal.ZERO
        for (product in products) {
            total += product.productValue
        }
        return total
    }

    // Definindo a propriedade total como var para permitir a atualização
    var total: BigDecimal = BigDecimal.ZERO
        private set

    // Método para retornar o total formatado como uma string
    fun getTotalAsString(): String {
        return total.toString()
    }
}
