package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.shabelnikd.rickandmorty.R
import com.valentinilk.shimmer.shimmer
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun ShimmeringCharacterPlaceholder(isShimmer: Boolean) {

    if (isShimmer) {
        Row(
            modifier = Modifier
                .shimmer()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
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
                            .size(6.dp)
                            .background(color = Color.LightGray, shape = CircleShape)
                    )

                }

                Spacer(
                    modifier = Modifier
                        .padding(vertical = 3.dp)
                        .size(width = 30.dp, height = 6.dp)
                        .background(
                            color = Color.LightGray,
                            shape = MaterialTheme.shapes.small
                        )
                )

            }

            Box(
                modifier = Modifier.minimumInteractiveComponentSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_unfilled),
                    contentDescription = "Favorite"
                )
            }

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(), thickness = 0.6.dp
        )
    }
}