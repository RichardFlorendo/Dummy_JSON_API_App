package com.example.serino_dev_assessment.model

import android.os.Parcelable
import com.example.serino_dev_assessment.model.database.ProductEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val brand: String?,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val images: List<String>
) : Parcelable

data class ProductResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        category = this.category,
        brand = this.brand?: "Unknown",
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating,
        stock = this.stock,
        thumbnail = this.thumbnail,
        images = if (this.images.isNotEmpty()) this.images.split("|") else emptyList()
    )
}