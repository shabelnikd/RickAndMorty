package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterWithFavoriteStatus
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseListItem

@Composable
fun CharacterListItem(
    character: CharacterWithFavoriteStatus,
    modifier: Modifier = Modifier,
    animate: Boolean,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onClick: () -> Unit = {}
) {
    val statusMatch = when (character.characterModel.status.lowercase()) {
        "dead" -> Color(0xFF9C0303)
        "alive" -> Color(0xFF02B715)
        else -> Color(0xFF7B2589)
    }

    BaseListItem(
        itemKey = character.characterModel.id,
        modifier = modifier,
        animate = animate,
        onClick = onClick,
        animateInitialScale = 0.95f,
        animateTargetScale = 1.15f
    ) {

        AsyncImage(
            model = character.characterModel.image,
            filterQuality = FilterQuality.Low,
            contentDescription = "Character image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = character.characterModel.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = character.characterModel.species,
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(
                    modifier = Modifier
                        .size(6.dp)
                        .background(color = statusMatch, shape = CircleShape)
                )

            }
            Text(
                text = character.characterModel.gender,
                style = MaterialTheme.typography.labelSmall
            )

        }
        FavoriteToggleButton(isFavorite = character.isFavorite, modifier = Modifier) {
            onToggleFavorite(character.characterModel.id, character.isFavorite)
        }

    }

}




