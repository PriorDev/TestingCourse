package com.plcoding.testingcourse.shopping.data

import com.plcoding.testingcourse.core.domain.Product
import com.plcoding.testingcourseexamples.part1.domain.ShoppingCartCache

class ShoppingCartCacheFake: ShoppingCartCache {
    private var itemsCache = emptyList<Product>()
    override fun saveCart(items: List<Product>) {
        itemsCache = items
    }

    override fun loadCart(): List<Product> {
        return itemsCache
    }

    override fun clearCart() {
        itemsCache = emptyList()
    }
}