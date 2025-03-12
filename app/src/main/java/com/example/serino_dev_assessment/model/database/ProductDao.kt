package com.example.serino_dev_assessment.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("DELETE FROM products")
    suspend fun clearProducts() // Clears all products from the database

    @Query("SELECT * FROM products ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getProductsByPage(limit: Int, offset: Int): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>) //Inserts all data

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}