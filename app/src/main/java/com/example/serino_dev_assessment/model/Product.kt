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
    val price: Double,
    val thumbnail: String
) : Parcelable

data class ProductResponse(
    val products: List<Product>, // Corrected field name
    val total: Int, // Matches API response
    val skip: Int, // Matches API response
    val limit: Int // Matches API response
)

// Converts Product to Room Entity
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        category = this.category,
        price = this.price,
        thumbnail = this.thumbnail
    )
}

// Converts Room Entity to Product
fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        category = this.category,
        price = this.price,
        thumbnail = this.thumbnail
    )
}
