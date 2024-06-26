package br.pucpr.product.roles.controller.requests

import br.pucpr.product.roles.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateRoleRequest(
    @Pattern(regexp = "^[A-Z][A-Z0-9]+\$")
    val name: String?,

    @NotBlank
    val description: String?
) {
    fun toRole() = Role(name = name!!, description = description!!)
}