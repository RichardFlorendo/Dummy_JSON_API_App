package com.example.serino_dev_assessment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String
) : Parcelable

data class ProductResponse(
    val products: List<Product>, // Corrected field name
    val total: Int, // Matches API response
    val skip: Int, // Matches API response
    val limit: Int // Matches API response
)

//the entire JSON list from the URL, stored in the Product list