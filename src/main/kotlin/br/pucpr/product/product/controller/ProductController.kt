package br.pucpr.product.product.controller


import br.pucpr.product.product.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {
    @GetMapping
    fun getAllProducts(@RequestParam sortDir: String? = "ASC", @RequestParam name: String? = null): ResponseEntity<List<ProductResponse>> {
        val products = productService.findAll()
        val sortedProducts = if (sortDir == "DESC") products.sortedByDescending { it.name } else products.sortedBy { it.name }
        val filteredProducts = name?.let { sortedProducts.filter { it.name.contains(name, ignoreCase = true) } } ?: sortedProducts
        return ResponseEntity.ok(filteredProducts.map { ProductResponse(it) })
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.findById(id)
        return if (product != null) ResponseEntity.ok(ProductResponse(product)) else ResponseEntity.notFound().build()
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createProduct(@RequestBody productRequest: CreateProductRequest): ResponseEntity<ProductResponse> {
        val createdProduct = productService.createProduct(productRequest.toProduct())
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse(createdProduct))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateProduct(@PathVariable id: Long, @RequestBody updatedProductRequest: CreateProductRequest): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(id, updatedProductRequest.toProduct())
        return if (updatedProduct != null) ResponseEntity.ok(ProductResponse(updatedProduct)) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}

