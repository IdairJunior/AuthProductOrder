package br.pucpr.product.users

import br.pucpr.product.exception.BadRequestException

enum class SortDir {
    ASC, DESC;

    companion object {
        fun findOrThrow(sortDir: String) =
            values().find { it.name == sortDir.uppercase() }
                ?: throw BadRequestException("Invalid sort dir!")
    }
}