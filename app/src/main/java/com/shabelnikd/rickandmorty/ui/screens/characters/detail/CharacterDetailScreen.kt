package com.shabelnikd.rickandmorty.ui.screens.characters.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import com.shabelnikd.rickandmorty.ui.components.FavoriteToggleButton
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseDetailScreen
import com.shabelnikd.rickandmorty.ui.core.base.components.StyledTextKeyValueList
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val vm = koinViewModel<CharacterDetailScreenVM>()

    LaunchedEffect(characterId) {
        if (characterId != -1) {
            vm.loadCharacterDetails(characterId = characterId)
        } else {
            navController.popBackStack()
        }
    }

    val characterState by vm.characterModelState.collectAsStateWithLifecycle()
    val isFavorite by vm.isFavorite.collectAsStateWithLifecycle()
    val dominantColor by vm.dominantColor.collectAsStateWithLifecycle()


    BaseDetailScreen<CharacterModel>(
        modifier = modifier,
        itemKey = characterId,
        itemState = characterState
    ) { item ->
        Box(
            modifier = Modifier, contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = "Character image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = Shapes().small),
            )

            FavoriteToggleButton(
                isFavorite = isFavorite == true, modifier = Modifier
            ) {
                vm.toggleFavoriteStatus(item.id, isFavorite == true)
            }

        }

        Spacer(modifier = Modifier.size(4.dp))

        Column(
            modifier = Modifier.align(alignment = Alignment.End),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StyledTextKeyValueList(
                color = dominantColor ?: MaterialTheme.colorScheme.onBackground,
                filterValue = "unknown",

                "Имя: " to item.name,
                "Пол: " to item.gender,
                "Раса: " to item.species,
                "Статус: " to item.status,
                "Обитает: " to item.origin.name.replace(
                    regex = "\\(Replacement Dimension\\)".toRegex(),
                    replacement = "\uD83C\uDF00"
                ),
                "Локация: " to item.characterLocation.name.replace(
                    regex = "\\(Replacement Dimension\\)".toRegex(),
                    replacement = "\uD83C\uDF00"
                )
            )
        }
    }
}
