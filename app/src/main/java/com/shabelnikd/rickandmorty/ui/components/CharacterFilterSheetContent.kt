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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
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
    val statusOptions =
        listOf<Pair<String?, String>>(
            null to "Все",
            "alive" to "Жив",
            "dead" to "Мётрв",
            "unknown" to "Неизвестно",
        )
    val genderOptions = listOf<Pair<String?, String>>(
        null to "Любой",
        "female" to "Женский",
        "male" to "Мужской",
        "genderless" to "Нет пола",
        "unknown" to "Неизвестный",
    )




    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Фильтры", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.size(16.dp))

        Text("Статус", style = MaterialTheme.typography.titleSmall)
        Spacer(
            modifier = Modifier.size(8.dp)
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            statusOptions.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = status == option.first,
                    onClick = { onStatusChange(option.first) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = statusOptions.size
                    )
                ) {
                    Text(
                        text = option.second,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))


        Text("Пол", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.size(8.dp))

        FilterDropdown(
            options = genderOptions,
            selectedOption = gender,
            onOptionSelected = onGenderChange,
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
    options: List<Pair<T, String>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedDisplayText = options.find { it.first == selectedOption }?.second ?: "Неизвестно"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Material3TextField(
            value = selectedDisplayText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = 0.dp, y = (-5).dp),
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, text) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onOptionSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
