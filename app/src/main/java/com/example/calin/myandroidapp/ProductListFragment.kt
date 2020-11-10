package com.example.calin.myandroidapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        Log.i(TAG, "here1")
        item_list.adapter = productListAdapter
        Log.i(TAG, "here2")
        productModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        Log.i(TAG, "here3")
        productModel.products.observe(viewLifecycleOwner, { value ->
            productListAdapter.products = value
        })
        Log.i(TAG, "here4")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}