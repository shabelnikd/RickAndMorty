package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.shabelnikd.rickandmorty.domain.models.characters.Character

@Composable
fun CharacterListItem(character: Character, modifier: Modifier, onClick: () -> Unit) {
    val statusMatch = when (character.status.lowercase()) {
        "dead" -> "мертв" to Color(0xFF9C0303)
        "alive" -> "жив" to Color(0xFF02B715)
        else -> "неизвестно" to Color(0xFF7B2589)
    }


    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = character.image,
            filterQuality = FilterQuality.Low,
            contentDescription = "Character image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = character.name, style = MaterialTheme.typography.titleMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = character.species, style = MaterialTheme.typography.bodyMedium)

                Spacer(
                    modifier = Modifier
                        .size(6.dp)
                        .background(color = statusMatch.second, shape = CircleShape)
                )
            }

            Text(text = character.gender, style = MaterialTheme.typography.bodySmall)

        }
    }


}
