package com.example.calin.myandroidapp.todo.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.calin.myandroidapp.R
import com.example.calin.myandroidapp.core.TAG
import com.example.calin.myandroidapp.todo.data.Product
import com.example.calin.myandroidapp.todo.product.ProductEditFragment
import kotlinx.android.synthetic.main.view_product.view.*

class ProductListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var products = emptyList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onProductClick: View.OnClickListener;

    init {
        onProductClick = View.OnClickListener { view ->
            val product = view.tag as Product
            fragment.findNavController().navigate(R.id.fragment_product_edit, Bundle().apply {
                putString(ProductEditFragment.PRODUCT_ID, product._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_product, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val product = products[position]
        holder.itemView.tag = product
        holder.textView.text = product.toString()
        holder.itemView.setOnClickListener(onProductClick)
    }

    override fun getItemCount() = products.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
