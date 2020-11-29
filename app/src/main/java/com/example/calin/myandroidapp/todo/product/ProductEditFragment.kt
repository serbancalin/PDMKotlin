package com.example.calin.myandroidapp.todo.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.calin.myandroidapp.R
import com.example.calin.myandroidapp.core.TAG
import com.example.calin.myandroidapp.todo.data.Product
import kotlinx.android.synthetic.main.fragment_product_edit.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProductEditFragment : Fragment() {
    companion object {
        const val PRODUCT_ID = "PRODUCT_ID"
    }

    private lateinit var viewModel: ProductEditViewModel
    private var productId: String? = null
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(PRODUCT_ID)) {
                productId = it.getString(PRODUCT_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_product_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save product")
            val i = product
            if (i != null) {
                i.name = product_name.text.toString()
                i.price = product_price.text.toString().toDouble()
                viewModel.saveOrUpdateProduct(i)
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ProductEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        }
        val id = productId
        if (id == null) {
            product = Product("", "", 0.0)
        } else {
            viewModel.getProductById(id).observe(viewLifecycleOwner) {
                Log.v(TAG, "update items")
                if (it != null) {
                    product = it
                    product_name.setText(it.name)
                    product_price.setText(it.price.toString())
                }
            }
        }
    }
}