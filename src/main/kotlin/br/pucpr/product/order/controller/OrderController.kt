package br.pucpr.product.order.controller

import br.pucpr.product.order.Order
import br.pucpr.product.order.OrderService
import br.pucpr.product.orderProduct.CreateOrderAndProductRequest
import br.pucpr.product.product.Product
import br.pucpr.product.product.ProductService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.hibernate.internal.util.type.PrimitiveWrapperHelper.LongDescriptor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name = "Product")
class OrderController(
    private val orderService: OrderService,
    private val productService: ProductService
) {
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderService.findAll().map { OrderResponse(it) })

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderResponse> {
        val order = orderService.findById(id)
        return if (order != null) ResponseEntity.ok(OrderResponse(order)) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createOrder(@RequestBody orderRequest: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val createdOrder = orderService.createOrder(orderRequest.toOrder())
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse(createdOrder))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody updatedOrderRequest: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrder(id, updatedOrderRequest.toOrder())
        return if (updatedOrder != null) ResponseEntity.ok(OrderResponse(updatedOrder)) else ResponseEntity.notFound()
            .build()
    }

    @SecurityRequirement(name = "Product")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Void> {
        orderService.deleteOrder(id)
        return ResponseEntity.noContent().build()
    }

    @SecurityRequirement(name = "Product")
    @PostMapping("/{id}/products/{productId}")
    fun addProductToOrder(
        @PathVariable id: Long?,
        @PathVariable productId: Long,
        @RequestParam quantity: Int
    ): ResponseEntity<OrderResponse> {

        val order = if (id != null) {
            orderService.findById(id) ?: return ResponseEntity.notFound().build()
        } else {
            val order = Order(
                null,
                ""
            )
            orderService.createOrder(order);
        }

        val product = productService.findById(productId) ?: return ResponseEntity.notFound().build()
        val updatedOrder = orderService.addProductToOrder(order, product, quantity)
        return ResponseEntity.ok(OrderResponse(updatedOrder))
    }


    @DeleteMapping("/{id}/products/{productId}")
    fun removeProductFromOrder(@PathVariable id: Long, @PathVariable productId: Long): ResponseEntity<OrderResponse> {
        val order = orderService.findById(id) ?: return ResponseEntity.notFound().build()
        val updatedOrder = orderService.removeProductFromOrder(order, productId)
        return ResponseEntity.ok(OrderResponse(updatedOrder))
    }

    @SecurityRequirement(name = "Product")
    @PostMapping("/create-and-add-product")
    fun createOrderAndAddProduct(
        @RequestBody request: CreateOrderAndProductRequest
    ): ResponseEntity<OrderResponse> {
        // Cria a ordem com base nos dados fornecidos
        val order = orderService.createOrder(request.order.toOrder())

        // Cria o produto com base nos dados fornecidos
        val product = productService.createProduct(request.product.toProduct())

        // Adiciona o produto à ordem
        val updatedOrder = orderService.addProductToOrder(order, product, request.quantity)

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse(updatedOrder))
    }



}
