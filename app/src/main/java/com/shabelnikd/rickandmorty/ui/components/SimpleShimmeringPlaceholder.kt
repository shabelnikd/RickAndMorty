package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun SimpleShimmeringPlaceholder(isShimmer: Boolean) {

    if (isShimmer) {

        Row(
            modifier = Modifier
                .shimmer()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                val width = Random.nextInt(100..200)

                Spacer(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .size(width = width.dp, height = 10.dp)
                        .background(
                            color = Color.LightGray,
                            shape = MaterialTheme.shapes.small
                        )
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 7.dp)
                            .size(width = 40.dp, height = 8.dp)
                            .background(
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.small
                            )
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 7.dp)
                            .size(width = 40.dp, height = 8.dp)
                            .background(
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
            }

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(), thickness = 0.6.dp
        )
    }
}