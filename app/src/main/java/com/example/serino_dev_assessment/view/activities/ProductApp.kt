package com.example.serino_dev_assessment.view.activities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.view.Screen
import com.example.serino_dev_assessment.viewmodel.MainViewModel

@Composable
fun ProductApp(navController: NavHostController){
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.productState

    NavHost(navController = navController,
        startDestination = Screen.ProductScreen.route){
        composable(route = Screen.ProductScreen.route){
            ProductListScreen(viewState = viewState,
                navigateToDetail = {
                    //This part is responsible for passing data from the current screen to the details screen
                    //It utilizes he savedStateHandle, which is a component of the Compose navigation framework
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("prod", it) //Stores the product as "prod"
                    navController.navigate(Screen.DetailScreen.route) //navigates to the details screen
                })
        }

        composable(route = Screen.DetailScreen.route){
            val product = navController.previousBackStackEntry //retrieves data from the previous backstack
                ?.savedStateHandle //stores data across screens
                ?.get<Product>("prod") //retrieve data with type "Product" with key "prod"
                ?: Product(0, "", "", "", 0.0, "") //if "prod" is empty, create an empty Product
                ProductDetailScreen(product = product)
        }
    }
}