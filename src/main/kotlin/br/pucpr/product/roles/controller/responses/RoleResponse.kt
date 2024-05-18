package br.pucpr.product.roles.controller.responses

import br.pucpr.product.roles.Role

data class RoleResponse(
    val name: String,
    val description: String
) {
    constructor(role: Role) : this(name = role.name, description = role.description)
}
