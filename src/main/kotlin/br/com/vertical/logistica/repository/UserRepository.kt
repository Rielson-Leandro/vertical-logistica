package br.com.vertical.logistica.repository

import br.com.vertical.logistica.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>