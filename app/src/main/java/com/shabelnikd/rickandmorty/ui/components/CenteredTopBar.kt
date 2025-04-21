package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(text: String, imageVector: ImageVector, onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Button"
                )
            }
        }
    )
}