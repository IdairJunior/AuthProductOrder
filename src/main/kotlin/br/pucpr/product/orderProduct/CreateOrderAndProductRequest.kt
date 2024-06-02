package br.pucpr.product.orderProduct

import br.pucpr.product.order.controller.CreateOrderRequest
import br.pucpr.product.product.controller.CreateProductRequest

data class CreateOrderAndProductRequest(
    val order: CreateOrderRequest,
    val product: CreateProductRequest,
    val quantity: Int
)

