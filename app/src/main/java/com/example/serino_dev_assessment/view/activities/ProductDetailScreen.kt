package com.example.serino_dev_assessment.view.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.serino_dev_assessment.model.Product

@Composable
fun ProductDetailScreen(product: Product){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = product.category, textAlign = TextAlign.Center)
        Image(painter = rememberAsyncImagePainter(model = product.thumbnail),
            //loads image with one line of code, from io implementation in build.gradle
            contentDescription = "${product.category} Thumbnail",
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f))

        Text(text = product.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.verticalScroll(rememberScrollState())//allows to scroll through the text if it is more than the space
        )

    }
}