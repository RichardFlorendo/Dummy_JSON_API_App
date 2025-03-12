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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Text(text = "ERROR OCCURRED", modifier.align(Alignment.Center))
            }
            else->{

                //Display Categories when not loading and no error
                ProductScreen(
                    products = viewState.list,
                    viewState,
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
    viewState: MainViewModel.ProductState,
    navigateToDetail: (Product) -> Unit,
    fetchNextPage: () -> Unit,
    fetchPreviousPage: () -> Unit,
    currentPage: Int)
{

    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.Bold),
            text = "Product List",
            textAlign = TextAlign.Center,
            fontSize = 50.sp
        )

        LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.weight(1f)) {
            items(products) { product ->
                ProductItem(product, navigateToDetail = navigateToDetail)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { fetchPreviousPage() },
                enabled = currentPage > 1 && !viewState.isFromCache
            ) {
                Text("Previous")
            }

            Button(
                onClick = { fetchNextPage() },
                enabled = !viewState.isFromCache
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
    Box(modifier =
        Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clickable { navigateToDetail(product) }, //allows the item to be clickable
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = rememberAsyncImagePainter(model = product.thumbnail),
                //loads image with one line of code, from io implementation in build.gradle
                contentDescription = product.description,
                contentScale = ContentScale.FillBounds
            )

            Column {
                Text(
                    text = product.title,
                    color = Color.Black,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = product.description,
                    color = Color.Black,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = product.price.toString(),
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}