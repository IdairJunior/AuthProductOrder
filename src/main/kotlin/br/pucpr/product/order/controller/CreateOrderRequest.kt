package br.pucpr.product.order.controller

import br.pucpr.product.order.Order
import jakarta.validation.constraints.NotBlank

data class CreateOrderRequest(
    @field:NotBlank
    val orderDescription: String?
) {
    fun toOrder() = Order(
        orderDescription = orderDescription!!
    )
}
