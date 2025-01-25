package com.joel.locations.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    value : String,
    onValueChange : (String) -> Unit,
    isEmpty : Boolean
) {
    Column {
        if(!isEmpty){
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "My Locations",
                            fontSize = 18.sp
                        )
                    },
                    actions = {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(
                                text = "Edit",
                                color = Color(0xFFFEB800),
                                fontSize = 16.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF142036)
                    ),
                    modifier = Modifier
                        .height(65.dp)
                )
            }
        }
        SearchBar(
            hint = "Find locations...",
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
) {
    var isHintDisplayed by remember { mutableStateOf(hint.isNotEmpty()) }

    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF132847), Color(0xFF142036))
                    ), RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .onFocusChanged { focusState ->
                    isHintDisplayed = !focusState.isFocused && value.isEmpty()
                }
                .height(30.dp)
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color(0xFF6C788E),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchAppBarPreview() {
    MaterialTheme {
        SearchBar(
            value = "",
            onValueChange = {},
            onSearch = {}
        )
    }
}

