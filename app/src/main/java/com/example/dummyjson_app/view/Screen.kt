package com.example.dummyjson_app.view

sealed class Screen(val route: String){
    object ProductScreen: Screen(route = "productscreen")
    object DetailScreen: Screen(route = "detailscreen")
}