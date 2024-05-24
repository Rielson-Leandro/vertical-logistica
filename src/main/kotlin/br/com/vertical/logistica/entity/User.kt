package br.com.vertical.logistica.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    val id: Long,
    val name: String
) {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    val orderEntities: MutableList<OrderEntity> = mutableListOf()
}
