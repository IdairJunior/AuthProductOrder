package br.pucpr.product.users.controller.responses

data class LoginResponse(
    val token: String,
    val user: UserResponse
)