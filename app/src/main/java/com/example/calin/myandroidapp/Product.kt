package com.example.calin.myandroidapp

data class Product(
    val id: String,
    val name: String,
    val price: Number
) {
    override fun toString(): String = "$name $price"
}
