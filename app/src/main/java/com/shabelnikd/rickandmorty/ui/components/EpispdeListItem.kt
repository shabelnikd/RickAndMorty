package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode

@Composable
fun EpisodeListItem(epidose: Episode, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = epidose.name, style = MaterialTheme.typography.titleMedium)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = epidose.episode, style = MaterialTheme.typography.bodyMedium)
                Text(text = "(${epidose.airDate})", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }
}