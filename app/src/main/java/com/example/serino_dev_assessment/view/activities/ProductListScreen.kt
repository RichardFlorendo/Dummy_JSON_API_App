package com.example.serino_dev_assessment.view.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.serino_dev_assessment.model.Product
import com.example.serino_dev_assessment.viewmodel.MainViewModel

@Composable
fun ProductListScreen(modifier: Modifier = Modifier,
                      viewState: MainViewModel.ProductState,
                      navigateToDetail: (Product) -> Unit){

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
                ProductScreen(products = viewState.list, navigateToDetail)
            }

        }
    }
}

@Composable
fun ProductScreen(products: List<Product>,
                   navigateToDetail: (Product) -> Unit){
    LazyVerticalGrid(columns = GridCells.Fixed(2), //displays items in a column with specific column numbers
        modifier = Modifier.fillMaxSize()
    ){
        items(products){//pass each item through the ProductItem function
                product -> //passes retrieved data to the function
            ProductItem (product = product, navigateToDetail)
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