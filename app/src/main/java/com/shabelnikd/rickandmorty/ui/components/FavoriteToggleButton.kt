package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.shabelnikd.rickandmorty.R

@Composable
fun FavoriteToggleButton(isFavorite: Boolean, modifier: Modifier, onToggleFavorite: () -> Unit) {

    val contentColor = Color(0xFF9C0303)

    val unfilledIcon = @Composable {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_unfilled),
            contentDescription = "Favorite"
        )
    }

    val filledIcon = @Composable {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_filled),
            contentDescription = "Favorite"
        )
    }

    IconToggleButton(
        modifier = modifier,
        checked = isFavorite == true, onCheckedChange = {
            onToggleFavorite()
        }, colors = IconButtonDefaults.iconToggleButtonColors().copy(
            checkedContentColor = contentColor, contentColor = contentColor
        )
    ) {
        if (isFavorite == true) filledIcon()
        else unfilledIcon()

    }

}