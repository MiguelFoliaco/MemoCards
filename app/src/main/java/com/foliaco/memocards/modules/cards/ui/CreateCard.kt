package com.foliaco.memocards.modules.cards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.MutableLiveData
import com.foliaco.memocards.modules.components.CardItemList
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.ColorText
import com.foliaco.memocards.modules.theme.ColorText2

@Composable
fun CrateCardScreen(viewModel: HomeScreenViewModel) {
    val memo: Memos by viewModel.memoCreate.observeAsState(
        initial = Memos(
            id = "",
            widget = false
        )
    )
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg == "Todo bien") {
        AlertDialog(
            text = {
                Text(text = "El memo se guardo correctamente", textAlign = TextAlign.Center)
            },
            onDismissRequest = {
                viewModel.isSuccessFullOrError.postValue("")
                viewModel.memoCreate.postValue(Memos(id = "", widget = false))
            },
            buttons = {
                TextButton(
                    onClick = {
                        viewModel.isSuccessFullOrError.postValue("")
                        viewModel.memoCreate.postValue(Memos(id = "", widget = false))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirmar", color = Color(0xFFFFFFFF))
                }
            },
            backgroundColor = MaterialTheme.colorScheme.background
        )
    } else if (msg == "Todo mal") {
        AlertDialog(
            text = {
                Text(text = "Ocurrió un error al guardar el memo", textAlign = TextAlign.Center)
            },
            onDismissRequest = {
                viewModel.isSuccessFullOrError.postValue("")
            },
            buttons = {
                TextButton(
                    onClick = {
                        viewModel.isSuccessFullOrError.postValue("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirmar", color = Color(0xFFFFFFFF))
                }
            },
            backgroundColor = MaterialTheme.colorScheme.background
        )
    }

    PreviewCard(viewModel, memo)
    Spacer(modifier = Modifier.padding(10.dp))
    CreateCardForm(viewModel, memo)
}

@Composable
fun PreviewCard(viewModel: HomeScreenViewModel, memo: Memos) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "Preview", modifier = Modifier.padding(bottom = 10.dp))
        CardItemList(modifier = Modifier, memo = memo, viewModel = viewModel, null)
    }
}


//
//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF232528)
@Composable
fun CreateCardForm(viewModel: HomeScreenViewModel, memo: Memos) {

    val isLoading: Boolean by viewModel.isLoadinCreateMemos.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ListLenguajes(viewModel, memo)
        InputWordKey(viewModel, memo)
        InputWordValue(viewModel, memo)
        InputWordReading(viewModel, memo)
        InputWordOn(viewModel, memo)
        InputWordKun(viewModel, memo)
        CheckWidget(viewModel, memo)
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = {
                if (isLoading) {

                } else if ((memo.value.orEmpty() != "") && (memo.value.orEmpty() != "") && memo.lenguajeId.orEmpty() != "") {
                    viewModel.saveMemoDb()
                }
            },
            enabled = (memo.value.orEmpty() != "") && (memo.value.orEmpty() != "") && memo.lenguajeId.orEmpty() != "",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = ColorText,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = ColorText2,
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = "Guardar")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListLenguajes(viewModel: HomeScreenViewModel, memo: Memos) {
    val list = viewModel.getLenguajes()
    val keys = list.keys.toList()
    if (viewModel.isLoadingMemos.value == true) {
        LinearProgressIndicator()
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        val isSelected = viewModel.memoCreate.value?.lenguajeId
        items(keys) {
            val item = list[it]
            if (item != null) {
                println("Memo Keys $item $it")
                Chip(
                    onClick = {
                        viewModel.memoCreate.postValue(memo.copy(lenguajeId = it))
                    },
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable { },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isSelected == it) MaterialTheme.colorScheme.primary else ColorText2
                    )
                ) {
                    Text(
                        text = "${item["title"]}",
                        color = if (isSelected == it) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun InputWordKey(viewModel: HomeScreenViewModel, memo: Memos) {

    var text by remember { mutableStateOf(memo.key.orEmpty()) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        text = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Word or sentences",
            style = TextStyle(
                color = ColorText2,
                fontSize = 17.sp,
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.memoCreate.postValue(memo.copy(key = text))
                if (text.length >= 14) {
                    viewModel.memoCreate.postValue(memo.copy(widget = false))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            placeholder = {
                Text(text = "Any lenguajes...", color = ColorText2)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color(0xFFD1D1D1),
                focusedTextColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF111111),
            ),
        )
    }
}

@Composable
fun InputWordValue(viewModel: HomeScreenViewModel, memo: Memos) {
    var text by remember { mutableStateOf(memo.value.orEmpty()) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        text = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Translate",
            style = TextStyle(
                color = ColorText2,
                fontSize = 17.sp,
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = text,
            onValueChange = {
                it
                text = it
                viewModel.memoCreate.postValue(memo.copy(value = text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color(0xFFD1D1D1),
                focusedTextColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF111111),
            ),
        )
    }
}

@Composable
fun InputWordOn(viewModel: HomeScreenViewModel, memo: Memos) {
    var text by remember { mutableStateOf(memo.reading_on.orEmpty()) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        text = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Reading On(Optional)",
            style = TextStyle(
                color = ColorText2,
                fontSize = 17.sp,
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.memoCreate.postValue(memo.copy(reading_on = text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color(0xFFD1D1D1),
                focusedTextColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF111111),
            ),
        )
    }
}

@Composable
fun InputWordKun(viewModel: HomeScreenViewModel, memo: Memos) {
    var text by remember { mutableStateOf(memo.reading_kun.orEmpty()) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        text = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Reading Kun(Optional)",
            style = TextStyle(
                color = ColorText2,
                fontSize = 17.sp,
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.memoCreate.postValue(memo.copy(reading_kun = text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color(0xFFD1D1D1),
                focusedTextColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF111111),
            ),
        )
    }
}

@Composable
fun InputWordReading(viewModel: HomeScreenViewModel, memo: Memos) {
    var text by remember { mutableStateOf(memo.reading.orEmpty()) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        text = ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Reading (Optional)",
            style = TextStyle(
                color = ColorText2,
                fontSize = 17.sp,
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.memoCreate.postValue(memo.copy(reading = text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            minLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color(0xFFD1D1D1),
                focusedTextColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF111111),
            ),
        )
    }
}

@Composable
fun CheckWidget(viewModel: HomeScreenViewModel, memo: Memos) {
    var check by remember { mutableStateOf(false) }
    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg === "Todo bien") {
        check = false
    }
    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Use in Widgets")
        Checkbox(checked = check, onCheckedChange = {
            if (memo.key!!.length >= 14) {
                check = false
                viewModel.memoCreate.postValue(memo.copy(widget = false))
            } else {
                check = it
                viewModel.memoCreate.postValue(memo.copy(widget = check))
            }
        })
    }
}