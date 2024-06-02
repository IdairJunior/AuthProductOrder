package br.pucpr.product.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(private val productRepository: ProductRepository) {
    fun findAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Product? = productRepository.findById(id).orElse(null)

    fun createProduct(product: Product): Product = productRepository.save(product)

    fun updateProduct(id: Long, updatedProduct: Product): Product? {
        val existingProduct = findById(id)
        return existingProduct?.let {
            it.name = updatedProduct.name
            it.price = updatedProduct.price
            it.description = updatedProduct.description
            productRepository.save(it)
        }
    }

    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }
}