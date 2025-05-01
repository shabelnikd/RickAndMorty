package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField as Material3TextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFilterSheetContent(
    status: String?,
    gender: String?,
    species: String?,
    type: String?,
    onStatusChange: (String?) -> Unit,
    onGenderChange: (String?) -> Unit,
    onSpeciesChange: (String?) -> Unit,
    onTypeChange: (String?) -> Unit,
    onCloseSheet: () -> Unit
) {
    val statusOptions = listOf(null, "alive", "dead", "unknown")
    val genderOptions = listOf(null, "female", "male", "genderless", "unknown")


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Фильтры", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.size(16.dp))

        FilterDropdown(
            label = "Статус",
            options = statusOptions,
            selectedOption = status,
            onOptionSelected = onStatusChange,
            getOptionText = { option -> option ?: "Любой" }
        )
        Spacer(modifier = Modifier.size(16.dp))


        FilterDropdown(
            label = "Пол",
            options = genderOptions,
            selectedOption = gender,
            onOptionSelected = onGenderChange,
            getOptionText = { option -> option ?: "Любой" }
        )
        Spacer(modifier = Modifier.size(16.dp))


        OutlinedTextField(
            value = species ?: "",
            onValueChange = { newValue ->
                onSpeciesChange(newValue.ifEmpty { null })
            },
            label = { Text("Вид") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))


        OutlinedTextField(
            value = type ?: "",
            onValueChange = { newValue ->
                onTypeChange(newValue.ifEmpty { null })
            },
            label = { Text("Тип") },
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun <T> FilterDropdown(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    getOptionText: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Material3TextField(
            value = getOptionText(selectedOption),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(getOptionText(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}