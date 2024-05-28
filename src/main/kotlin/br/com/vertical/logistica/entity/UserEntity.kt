package br.com.vertical.logistica.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class UserEntity(
    @Id
    val id: Long? = null,
    @field:NotBlank
    val name: String? = null,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val orderEntities: MutableList<OrderEntity> = mutableListOf()
)
