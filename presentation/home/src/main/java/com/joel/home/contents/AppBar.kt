package com.joel.home.contents

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    text : String,
    showLessClick : (Boolean) -> Unit,
    showMoreClick : (Boolean) -> Unit,
    hideDetails : Boolean
){

    TopAppBar(
        title = {
            Text(
                text = text,
                fontSize = 18.sp
            )
        },
        actions = {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!hideDetails) {
                    ClickableRowWithIconAndText(
                        text = "Show more",
                        icon = Icons.Filled.KeyboardArrowDown,
                        onClick = {
                            Log.d("----------------> HomeAppBar", "Show More clicked")
                            showMoreClick(true)
                        }
                    )
                } else {
                    ClickableRowWithIconAndText(
                        text = "Show less",
                        icon = Icons.Filled.KeyboardArrowUp,
                        onClick = {
                            Log.d("----------------> HomeAppBar", "Show More clicked")
                            showLessClick(false)
                        }
                    )
                }
            }
        },
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
            fontSize = 10.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp),
        )
    }
}



@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeAppBarPreview(){
    HomeAppBar(
        text = "Nairobi",
        hideDetails = true,
        showLessClick = {},
        showMoreClick = {}
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