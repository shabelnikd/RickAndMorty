package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(
    text: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: @Composable RowScope.() -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = { Text(text) },
        navigationIcon = {
            imageVector?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "Button"
                    )
                }
            }
        },
        actions = actions
    )
}