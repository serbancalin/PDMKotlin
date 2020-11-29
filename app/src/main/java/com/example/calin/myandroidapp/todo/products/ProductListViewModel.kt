package com.example.calin.myandroidapp.todo.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.calin.myandroidapp.core.TAG
import com.example.calin.myandroidapp.todo.data.Product
import com.example.calin.myandroidapp.todo.data.ProductRepository
import com.example.calin.myandroidapp.todo.data.local.TodoDatabase
import com.example.calin.myandroidapp.core.Result
import kotlinx.coroutines.launch

class ProductListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val products: LiveData<List<Product>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val productRepository: ProductRepository

    init {
        val productDao = TodoDatabase.getDatabase(application, viewModelScope).productDao()
        productRepository = ProductRepository(productDao)
        products = productRepository.products
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = productRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
