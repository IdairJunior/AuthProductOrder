package br.pucpr.product.product.controller

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CreateProductRequest(
    @field:NotBlank
    val name: String?,

    @field:NotNull
    @field:Positive
    val price: Double?,

    @field:NotBlank
    val description: String?
) {
    fun toProduct() = Product(
        name = name!!,
        price = price!!,
        description = description!!
    )
}
