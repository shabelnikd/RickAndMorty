package com.shabelnikd.rickandmorty.ui.screens.locations.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseDetailScreen
import com.shabelnikd.rickandmorty.ui.core.base.components.StyledTextKeyValueList
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationDetailScreen(locationId: Int, navController: NavController) {
    val vm = koinViewModel<LocationDetailScreenVM>()
    val locationState by vm.locationState.collectAsStateWithLifecycle()

    LaunchedEffect(locationId) {
        if (locationId != -1) {
            vm.getLocationById(locationId = locationId)
        } else {
            navController.popBackStack()
        }
    }

    BaseDetailScreen<Location>(
        itemKey = locationId,
        itemState = locationState,
        animate = false,
        progressDelayMillis = 500,
    ) { item ->
        Column(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StyledTextKeyValueList(
                color = MaterialTheme.colorScheme.onBackground,
                filterValue = "unknown",
                "Название локации: " to item.name,
                "Тип: " to item.type,
                "Измерение: " to item.dimension,
            )
        }
    }
}