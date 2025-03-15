package com.muraguri.design.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muraguri.design.R
import com.muraguri.design.ext.alertDialog
import com.muraguri.design.ext.textButton

@Composable
fun LocationsPermissionDialog(
    onRequestPermission: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    CustomDialog(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false }
    ) {
        EnableLocation(
            onRequestPermission = onRequestPermission,
            onDismissRequest = {showDialog = false}
        )
    }
}

@Composable
fun EnableLocation(
    onDismissRequest : () -> Unit,
    onRequestPermission: () -> Unit
){
    Column(Modifier.background(MaterialTheme.colorScheme.surface)) {

        var graphicVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { graphicVisible = true }

        AnimatedVisibility(
            visible = graphicVisible,
            enter = expandVertically(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                expandFrom = Alignment.CenterVertically,
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xE9EE8888),
                                Color(0xFFE4BD79),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                )
            }
        }

        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = R.string.locations_permission_title)) },
            text = { Text(stringResource(id = R.string.locations_permission_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        onDismissRequest()
                    },
                    modifier = Modifier.textButton(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFEB800),
                        contentColor = Color.White
                    )
                ) { Text(text = stringResource(R.string.request_locations_permission)) }
            },
            onDismissRequest = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationsPermissionDialogPreview(){
    LocationsPermissionDialog(
        onRequestPermission = {},
    )
}