package br.com.vertical.logistica.mappers

import br.com.vertical.logistica.dto.OrderDTO
import br.com.vertical.logistica.dto.ProductDTO
import br.com.vertical.logistica.entity.OrderEntity
import java.time.format.DateTimeFormatter

object OrderResponseMapper {

    private const val YYYY_MM_DD: String = "yyyy-MM-dd"
    private val dateFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD)

    fun convertToOrderDTO(entity: OrderEntity?, products: List<ProductDTO>): OrderDTO? {
        return entity?.run {
            OrderDTO(
                id = id ?: error("Order ID cannot be null"),
                total = total?.toString() ?: error("Total cannot be null"),
                date = date?.format(dateFormatter) ?: "",
                products = products
            )
        }
    }
}