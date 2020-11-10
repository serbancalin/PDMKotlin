package com.example.calin.myandroidapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel : ViewModel() {
    private val mutableProducts = MutableLiveData<List<Product>>().apply { value = emptyList() }

    val products: LiveData<List<Product>> = mutableProducts

    init {
        viewModelScope.launch {
            while (true) {
                val index = simulateNewProductNotification()
                createProduct(index)
            }
        }
    }

    fun createProduct(position: Int): Unit {
        val list = mutableListOf<Product>()
        list.addAll(mutableProducts.value!!)
        list.add(Product(position.toString(), "Product $position", 0.99))
        mutableProducts.value = list
    }

    var index = 100
    suspend fun simulateNewProductNotification(): Int = withContext(Dispatchers.Default) {
        delay(1000)
        return@withContext ++index
    }
}
