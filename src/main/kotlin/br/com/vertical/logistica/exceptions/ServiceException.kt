package br.com.vertical.logistica.exceptions

import org.springframework.http.HttpStatus

open class ServiceException(val status: HttpStatus, override val message: String) : RuntimeException(message)

sealed class InvalidParamsException(message: String, status: HttpStatus) : ServiceException(status, message) {
    object Invalid : InvalidParamsException("Os parâmetros informados são inválidos. Por favor, verifique as datas e tente novamente.", HttpStatus.BAD_REQUEST)
}

sealed class OrderNotFoundException(message: String, status: HttpStatus) : ServiceException(status, message) {
    class OrderNotFound(orderId: Long) : OrderNotFoundException("O pedido'$orderId' não foi encontrado. Por favor, verifique o ID e tente novamente.", HttpStatus.NOT_FOUND)
}

sealed class InvalidRequestException(message: String, status: HttpStatus) : ServiceException(status, message) {
    object Invalid : InvalidRequestException("Requisição inválida. Por favor, verifique os dados e tente novamente.", HttpStatus.BAD_REQUEST)
}