package com.example.calin.myandroidapp

import android.util.Log
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
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val products: LiveData<List<Product>> = mutableProducts
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createProduct(position: Int): Unit {
        val list = mutableListOf<Product>()
        list.addAll(mutableProducts.value!!)
        list.add(Product(position.toString(), "Product " + position, 0.99))
        mutableProducts.value = list
    }

    fun loadProducts() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...")
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableProducts.value = ProductRepository.getAll()
                Log.d(TAG, "loadProducts succeeded")
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadProducts failed", e)
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}
