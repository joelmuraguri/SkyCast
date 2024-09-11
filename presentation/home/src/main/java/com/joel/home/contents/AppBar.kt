package com.joel.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    text : String,
    onClick : (Boolean) -> Unit,
    hideDetails : Boolean
){

    TopAppBar(
        title = {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,

                ) },
        actions = {
            if (hideDetails){
                ClickableRowWithIconAndText(
                    text = "Show more",
                    icon = Icons.Filled.KeyboardArrowDown,
                    onClick = {
                        onClick(hideDetails)
                    }
                )
            } else {
                ClickableRowWithIconAndText(
                    text = "Show less",
                    icon = Icons.Filled.KeyboardArrowUp,
                    onClick = {
                        onClick(hideDetails)
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )

}

@Composable
fun ClickableRowWithIconAndText(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp),
            tint = Color.White,
        )
    }
}



@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeAppBarPreview(){
    HomeAppBar(
        text = "Nairobi",
        onClick = { /*TODO*/ },
        hideDetails = false
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ClickableRowWithIconAndTextPreview(){

    ClickableRowWithIconAndText(
        text = "Show more",
        icon = Icons.Filled.KeyboardArrowDown
    ) {

    }
}