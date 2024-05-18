package br.pucpr.product

import br.pucpr.product.roles.Role
import br.pucpr.product.roles.RoleRepository
import br.pucpr.product.users.User
import br.pucpr.product.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val rolesRepository: RoleRepository,
    val userRepository: UserRepository
) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole =
            rolesRepository.findByName("ADMIN") ?: rolesRepository
                .save(Role(name = "ADMIN", description = "System administrator"))
                .also {
                    rolesRepository.save(Role(name = "USER", description = "Paying user"))
                    log.info("ADMIN and USER roles created")
                }

        if (userRepository.findByRole("ADMIN").isEmpty()) {
            val admin = User(
                email = "admin@product.com",
                password = "admin",
                name = "Auth Server Administrator",
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
            log.info("Admin user created")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Bootstrapper::class.java)
    }
}