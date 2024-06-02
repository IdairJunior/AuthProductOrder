package br.pucpr.product.order

import br.pucpr.product.orderProduct.OrderProduct
import br.pucpr.product.product.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(private val orderRepository: OrderRepository) {
    fun findAll(): List<Order> = orderRepository.findAll()

    fun findById(id: Long): Order? = orderRepository.findById(id).orElse(null)

    fun createOrder(order: Order): Order = orderRepository.save(order)

    fun updateOrder(id: Long, updatedOrder: Order): Order? {
        val existingOrder = findById(id)
        return existingOrder?.let {
            it.orderDescription = updatedOrder.orderDescription
            orderRepository.save(it)
        }
    }

    fun deleteOrder(id: Long) {
        orderRepository.deleteById(id)
    }

    fun addProductToOrder(order: Order, product: Product, quantity: Int): Order {
        val orderProduct = OrderProduct(product = product, order = order, quantity = quantity)
        order.products.add(orderProduct)
        return orderRepository.save(order)
    }



    fun removeProductFromOrder(order: Order, productId: Long): Order {
        order.products.removeIf { it.product.id == productId }
        return orderRepository.save(order)
    }
}
