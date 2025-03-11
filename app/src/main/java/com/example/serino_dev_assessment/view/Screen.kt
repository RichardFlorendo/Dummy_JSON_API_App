package com.example.serino_dev_assessment.view

sealed class Screen(val route: String){
    object ProductScreen: Screen(route = "productscreen")
    object DetailScreen: Screen(route = "detailscreen")
}