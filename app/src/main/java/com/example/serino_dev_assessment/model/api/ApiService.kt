package com.example.serino_dev_assessment.model.api

import com.example.serino_dev_assessment.BuildConfig
import com.example.serino_dev_assessment.model.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl(BuildConfig.PRODUCT_API_BASE_URL) //baseURL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val productService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService{
    @GET("products") //appended to the end of baseURL
    suspend fun getProducts(
        @Query("limit") limit: Int = 10, //Default to 10 items per request
        @Query("skip") skip: Int = 0    //Default to skipping 0 items
    ):ProductResponse //Adds to the Product.kt list

}