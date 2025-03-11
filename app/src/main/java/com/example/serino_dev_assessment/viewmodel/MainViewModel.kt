package com.example.serino_dev_assessment.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.model.api.productService
import kotlinx.coroutines.launch

class MainViewModel :ViewModel(){

    private val _productState = mutableStateOf(ProductState()) //checks the states of values on RecipeState
    val productState: State<ProductState> = _productState //updates the state when data is gotten

    init { //Automatically starts everytime MainViewModel is loaded in RecipeScreen
        fetchProducts()
    }

    private fun fetchProducts(){
        viewModelScope.launch {// launches a coroutine (suspend functions or asynchronous)
            try { //best oractice for internet GET and PUSH
                val response = productService.getProducts() //calling the suspend function for internet gotten data
                Log.d("API Fetch", response.toString())
                Log.d("API Fetch", "Response Body: ${response.products}")
                _productState.value = _productState.value.copy( //"Get the current state and change the states"
                    list = response.products, //populates list
                    loading = false, //not loading
                    error = null //no error
                )
            }
            catch (e: Exception){
                e.message?.let { Log.d("API Fetch", it) }
                _productState.value = _productState.value.copy(
                    loading = false, //not loading anymore, but with error
                    error = "Error fetching Categories ${e.message}"
                )
            }
        }
    }

    data class ProductState( //contains loading status, if list is not empty, and if there is error
        val loading: Boolean = true,
        val list: List<Product> = emptyList(),
        val error: String? = null
    )
}