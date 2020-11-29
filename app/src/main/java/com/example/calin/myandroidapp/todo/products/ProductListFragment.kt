package com.example.calin.myandroidapp.todo.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.calin.myandroidapp.R
import com.example.calin.myandroidapp.auth.data.AuthRepository
import com.example.calin.myandroidapp.core.TAG
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.fragment_login)
            return;
        }
        setupProductList()
        fab.setOnClickListener {
            Log.v(TAG, "add new product")
            findNavController().navigate(R.id.fragment_product_edit)
        }
    }
    private fun setupProductList() {
        productListAdapter = ProductListAdapter(this)
        product_list.adapter = productListAdapter
        productModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        productModel.products.observe(viewLifecycleOwner) { products ->
            Log.i(TAG, "update products")
            productListAdapter.products = products
            Log.i(TAG, "update products done")
        }
        productModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
            Log.i(TAG, "update loading done")
        }
        productModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        productModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}