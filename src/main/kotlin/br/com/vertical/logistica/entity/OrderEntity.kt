    package br.com.vertical.logistica.entity

    import com.fasterxml.jackson.annotation.JsonIgnore
    import java.math.BigDecimal
    import java.time.LocalDate
    import javax.persistence.*
    import javax.validation.constraints.NotNull

    @Entity
    data class OrderEntity(
        @Id
        val id: Long? = null,
        @field:NotNull
        val date: LocalDate? = null,
        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonIgnore
        val user: UserEntity? = null,
        @field:NotNull
        var total: BigDecimal? = null,
        @ElementCollection
        @CollectionTable(name = "Order_products", joinColumns = [JoinColumn(name = "order_id")])
        @Embedded
        val products: MutableList<ProductEntity> = mutableListOf(),
    )