package br.com.vertical.logistica.repository

import br.com.vertical.logistica.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository : JpaRepository<UserEntity, Long>