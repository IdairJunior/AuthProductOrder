package br.pucpr.product.users.controller.responses

import br.pucpr.product.users.User

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
) {
    constructor(user: User) : this(user.id!!, user.email, user.name)
}