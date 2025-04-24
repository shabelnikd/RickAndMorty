package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun StyledTextKeyValue(
    rowModifier: Modifier,
    key: String,
    value: String,
    color: Color
) {

    val textStyle = MaterialTheme.typography.bodyLarge

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = key,
            fontStyle = FontStyle.Italic,
            style = textStyle,
            color = color
        )

        Text(
            text = value,
            style = textStyle,
            color = color
        )

        val brush = Brush.linearGradient(listOf<Color>(Color.Transparent, color))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(0.6.dp)
                .background(brush = brush)
        )

    }


}
