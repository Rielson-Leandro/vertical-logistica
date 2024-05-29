package br.com.vertical.logistica

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class VerticalLogisticaApplication

fun main(args: Array<String>) {
	runApplication<VerticalLogisticaApplication>(*args)
}
