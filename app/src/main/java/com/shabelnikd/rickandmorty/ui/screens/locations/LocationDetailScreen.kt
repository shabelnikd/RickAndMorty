package com.shabelnikd.rickandmorty.ui.screens.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.vm.locations.LocationDetailScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationDetailScreen(locationId: Int, navController: NavController) {
    val vm = koinViewModel<LocationDetailScreenVM>()
    val scope = rememberCoroutineScope()
    val locationState by vm.locationState.collectAsStateWithLifecycle()
    val locationNameState = remember { mutableStateOf("") }

    LaunchedEffect(scope) {
        vm.getLocationById(locationId = locationId)
    }

    Scaffold(
        topBar = {
            CenteredTopBar(
                text = locationNameState.value,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (locationState) {
                is BaseViewModel.UiState.Success<Location> -> {
                    val location =
                        (locationState as BaseViewModel.UiState.Success<Location>).data

                    locationNameState.value = location.name

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val textStyle = MaterialTheme.typography.bodyLarge

                            Row {
                                Text(
                                    text = "Название локации: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = location.name, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Тип: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = location.type, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Dimension: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = location.dimension, style = textStyle)
                            }
                        }
                    }

                }

                is BaseViewModel.UiState.NotLoaded -> {
                    Text("Еще не загружено")
                }

                else -> {
                    Text("Ошибка")
                }
            }
        }

    }


}