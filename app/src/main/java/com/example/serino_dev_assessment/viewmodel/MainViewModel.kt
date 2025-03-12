package com.example.serino_dev_assessment.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.model.api.productService
import com.example.serino_dev_assessment.model.database.AppDatabase
import com.example.serino_dev_assessment.model.database.ProductEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = AppDatabase.getDatabase(application).productDao()

    private val _productState = mutableStateOf(ProductState()) //checks the states of values on RecipeState
    val productState: State<ProductState> = _productState //updates the state when data is gotten

    var currentPage = 1 // Tracks which page is loaded

    init { //Automatically starts everytime MainViewModel is loaded in RecipeScreen
        fetchProducts(currentPage)
    }

    fun fetchProducts(page: Int){
        if (page < 1) return // Prevent invalid pages
        currentPage = page  // Ensure the ViewModel updates the page number

        val limit = 10
        val offset = (page - 1) * limit
        Log.d("Pagination", "Fetching page: $page, Offset: $offset") // Check correct page

        viewModelScope.launch {// launches a coroutine (suspend functions or asynchronous)
            try {
                // Fetch from API
                val response = productService.getProducts(limit, offset)
                val products = response.products.map {
                    ProductEntity(it.id, it.title, it.description, it.category, it.price, it.thumbnail)
                }

                // Cache the new data
                productDao.insertAll(products)

                Log.d("API Fetch", "Fetched ${response.products.size} products from API (Page: $page)")
                _productState.value = _productState.value.copy(
                    list = response.products,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                e.message?.let { Log.d("API Fetch", it) }
            }
        }
    }

    fun nextPage() {
        currentPage += 1
        Log.d("Pagination", "Next Page: $currentPage")
        fetchProducts(currentPage)
    }

    fun previousPage() {
        if (currentPage > 1) {
            currentPage -= 1
            Log.d("Pagination", "Previous Page: $currentPage")
            fetchProducts(currentPage)
        }
    }

    data class ProductState( //contains loading status, if list is not empty, and if there is error
        val loading: Boolean = true,
        val list: List<Product> = emptyList(),
        val error: String? = null
    )
}