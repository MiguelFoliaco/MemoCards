package com.foliaco.memocards.modules.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foliaco.memocards.modules.home.model.Lenguaje
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLenguaje(
    sheetState: SheetState,
    scope: CoroutineScope,
    listLenguajes: MutableMap<String, Map<String, Any>>,
    onSelect: (change: String) -> Unit
) {
    val keyNames = listLenguajes.keys.toList()
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState,

            ) {
            LazyColumn(
            ) {
                items(listLenguajes.size) {
                    val _item = listLenguajes[keyNames[it]]
                    ListItem(
                        modifier = Modifier.selectable(true) {
                            if (_item != null) {
                                onSelect(_item["title"] as String)
                            }
                        },
                        headlineContent = {
                            if (_item != null) {
                                Text(text = "${_item["title"]}")
                            }
                        },
                    )
                }
            }
        }
    }
}