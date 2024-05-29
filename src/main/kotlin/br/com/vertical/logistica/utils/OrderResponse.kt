package br.com.vertical.logistica.utils

import br.com.vertical.logistica.dto.OrderDTO
import br.com.vertical.logistica.dto.ProductDTO
import br.com.vertical.logistica.entity.OrderEntity
import java.time.format.DateTimeFormatter

object OrderResponse {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private fun OrderEntity.toOrderDTO(products: List<ProductDTO>): OrderDTO {
        return OrderDTO(
            id = requireNotNull(id) { "Order ID cannot be null" },
            total = requireNotNull(total) { "Total cannot be null" }.toString(),
            date = date?.format(dateFormatter) ?: "",
            products = products
        )
    }

    fun convertToOrderDTO(entity: OrderEntity?, products: List<ProductDTO>): OrderDTO? {
        return entity?.let {
            it.toOrderDTO(products)
        }
    }
}
