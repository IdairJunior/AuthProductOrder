package br.pucpr.product.product.controller

import br.pucpr.product.product.Product

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String
) {
    constructor(product: Product) : this(
        product.id!!,
        product.name,
        product.price,
        product.description
    )
}
