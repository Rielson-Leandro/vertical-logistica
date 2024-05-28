package br.com.vertical.logistica.repository

import br.com.vertical.logistica.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>