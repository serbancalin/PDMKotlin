package com.example.calin.myandroidapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_product_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductListFragment : Fragment() {
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            Log.v(TAG, "creating new product")
            productModel.products.value?.size?.let { productModel.createProduct(it) }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onActivityCreated")
        setupProductList()
    }

    private fun setupProductList() {
        productListAdapter = ProductListAdapter(this)
        item_list.adapter = productListAdapter
        productModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        productModel.products.observe(viewLifecycleOwner, { product ->
            Log.i(TAG, "update product")
            productListAdapter.products = product
        })
        productModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        productModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        productModel.loadProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}