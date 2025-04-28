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
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseListItem

@Composable
fun LocationListItem(
    location: Location,
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    BaseListItem(
        itemKey = location.id,
        onClick = onClick,
        modifier = modifier,
        animateTargetScale = 1.02f,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = location.name, style = MaterialTheme.typography.titleMedium)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = location.type, style = MaterialTheme.typography.bodyMedium)
                Text(text = "(${location.dimension})", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}