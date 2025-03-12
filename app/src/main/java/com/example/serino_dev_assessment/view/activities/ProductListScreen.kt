package com.example.serino_dev_assessment.view.activities

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.viewmodel.MainViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier,
    viewState: MainViewModel.ProductState,  // Explicitly pass state
    fetchProducts: (Int) -> Unit,  // Pass function references
    navigateToDetail: (Product) -> Unit,
    fetchNextPage: () -> Unit,
    fetchPreviousPage: () -> Unit,
    currentPage: Int){

    Box (modifier = Modifier.fillMaxSize()){
        when{//Checks if loading
            viewState.loading ->{ //Loading
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }
            viewState.error != null ->{ //Not Loading with Error
                Text(text = "ERROR OCCURRED")
            }
            else->{
                //Display Categories when not loading and no error
                ProductScreen(
                    products = viewState.list,
                    navigateToDetail,
                    fetchNextPage,
                    fetchPreviousPage,
                    currentPage)
            }

        }
    }
}

@Composable
fun ProductScreen(
    products: List<Product>,
    navigateToDetail: (Product) -> Unit,
    fetchNextPage: () -> Unit,
    fetchPreviousPage: () -> Unit,
    currentPage: Int) // Pass current page directly
{

    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.weight(1f)) {
            items(products) { product ->
                ProductItem(product, navigateToDetail = navigateToDetail)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { fetchPreviousPage() },
                enabled = currentPage > 1
            ) {
                Text("Previous")
            }

            Button(
                onClick = { fetchNextPage() }
            ) {
                Text("Next")
            }
        }
    }
}

//How each item should look like
@Composable
fun ProductItem(product: Product,
                navigateToDetail: (Product) -> Unit){
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
        .clickable { navigateToDetail(product) }, //allows the item to be clickable
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(painter = rememberAsyncImagePainter(model = product.thumbnail),
            //loads image with one line of code, from io implementation in build.gradle
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f))

        Text(text = product.title,
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}