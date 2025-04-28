package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseListItem

@Composable
fun EpisodeListItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BaseListItem(
        itemKey = episode.id,
        modifier = modifier,
        onClick = onClick,
        animateTargetScale = 1.02f
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = episode.name, style = MaterialTheme.typography.titleMedium)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = episode.episode, style = MaterialTheme.typography.bodyMedium)
                Text(text = "(${episode.airDate})", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }
}
