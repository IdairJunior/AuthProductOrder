package br.pucpr.product.order.controller

import br.pucpr.product.order.OrderService
import br.pucpr.product.product.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
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
    fun updateOrder(@PathVariable id: Long, @RequestBody updatedOrderRequest: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrder(id, updatedOrderRequest.toOrder())
        return if (updatedOrder != null) ResponseEntity.ok(OrderResponse(updatedOrder)) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Void> {
        orderService.deleteOrder(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/products/{productId}")
    fun addProductToOrder(@PathVariable id: Long, @PathVariable productId: Long, @RequestParam quantity: Int): ResponseEntity<OrderResponse> {
        val order = orderService.findById(id) ?: return ResponseEntity.notFound().build()
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
}
