package com.example.serino_dev_assessment.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.model.api.productService
import com.example.serino_dev_assessment.model.database.AppDatabase
import com.example.serino_dev_assessment.model.database.ProductEntity
import com.example.serino_dev_assessment.model.toDomain
import com.example.serino_dev_assessment.model.toEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = AppDatabase.getDatabase(application).productDao()

    private val _productState = mutableStateOf(ProductState()) // Holds UI state
    val productState: State<ProductState> = _productState // Exposes state to UI

    var currentPage = 1 // Tracks which page is loaded

    init {
        viewModelScope.launch {
            if (isOnline()) { // ✅ Check internet connection
                Log.d("Init", "Device is online. Fetching fresh data from API.")
                fetchProducts(currentPage) // Load fresh data from API
            } else {
                val cachedProducts = productDao.getAllProducts()
                if (cachedProducts.isNotEmpty()) {
                    Log.d("Init Cache", "Loading ${cachedProducts.size} cached products on startup (Offline Mode)")
                    _productState.value = _productState.value.copy(
                        list = cachedProducts.map { it.toDomain() },
                        loading = false
                    )
                }
            }
        }
    }

    fun fetchProducts(page: Int) {
        if (page < 1) return
        currentPage = page

        val limit = 10
        val offset = (page - 1) * limit

        _productState.value = _productState.value.copy(loading = true)

        viewModelScope.launch {
            if (isOnline()) {
                try {
                    // Fetch from API
                    val response = productService.getProducts(limit, offset)
                    val products = response.products.map {
                        ProductEntity(it.id, it.title, it.description, it.category, it.price, it.thumbnail)
                    }

                    productDao.insertProducts(products) // ✅ Cache new data
                    _productState.value = ProductState(
                        list = response.products,
                        loading = false,
                        error = null,
                        isFromCache = false,  // ✅ Data is fresh, not from cache
                        hasNextPage = response.products.isNotEmpty(), // ✅ Enable "Next" button only if products exist
                        hasPreviousPage = page > 1 // ✅ Enable "Previous" button only if page > 1
                    )
                } catch (e: Exception) {
                    _productState.value = _productState.value.copy(loading = false, error = e.message)
                }
            } else {
                Toast.makeText(getApplication(), "No internet connection. Loading cached data.", Toast.LENGTH_SHORT).show()

                val cachedProducts = productDao.getAllProducts()
                _productState.value = ProductState(
                    list = cachedProducts.map { it.toDomain() },
                    loading = false,
                    error = null,
                    isFromCache = true,  // ✅ Mark data as from cache
                    hasNextPage = false,  // ✅ Hide next button when offline
                    hasPreviousPage = false // ✅ Hide previous button when offline
                )
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


    private fun isOnline(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    data class ProductState(
        val loading: Boolean = true,
        val list: List<Product> = emptyList(),
        val error: String? = null,
        val stateId: Long = System.currentTimeMillis(), //Force recomposition
        val hasNextPage: Boolean = true,
        val hasPreviousPage: Boolean = true,
        val isFromCache: Boolean = false
    )
}