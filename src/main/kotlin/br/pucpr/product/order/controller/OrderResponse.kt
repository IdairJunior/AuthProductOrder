package br.pucpr.product.order.controller

import br.pucpr.product.order.Order
import br.pucpr.product.orderProduct.OrderProduct


data class OrderResponse(
    val id: Long,
    val orderNumber: String,
    val products: List<OrderProductResponse>
) {
    constructor(order: Order) : this(
        order.id!!,
        order.orderNumber,
        order.products.map { OrderProductResponse(it) }
    )
}

data class OrderProductResponse(
    val productId: Long,
    val productName: String,
    val quantity: Int
) {
    constructor(orderProduct: OrderProduct) : this(
        orderProduct.product.id!!,
        orderProduct.product.name,
        orderProduct.quantity
    )
}
