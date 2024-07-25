package com.foliaco.memocards.modules.cards.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.ColorText
import com.foliaco.memocards.modules.theme.ColorText2
import com.foliaco.memocards.screens.ListScreensHome

@Composable
fun UpdateCardScreen(viewModel: HomeScreenViewModel, id: String, navController: NavController) {
    val memos: MutableList<Memos> by viewModel.memos.observeAsState(mutableListOf())
    val memo: Memos by viewModel.memoCreate.observeAsState(Memos(id = id, widget = false))
    LaunchedEffect(Unit) {
        val _memo = memos.find { it.id == id }
        viewModel.memoCreate.postValue(_memo ?: Memos(id = id, widget = false))
    }

    if (memo.id == "") {
        return AlertDialog(
            text = {
                Text(text = "El memo seleccionado no existe", textAlign = TextAlign.Center)
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

    val msg: String by viewModel.isSuccessFullOrError.observeAsState(initial = "")
    if (msg == "Todo bien") {
        AlertDialog(
            text = {
                Text(text = "El memo se actualizo correctamente", textAlign = TextAlign.Center)
            },
            onDismissRequest = {
                viewModel.isSuccessFullOrError.postValue("")
                viewModel.memoCreate.postValue(Memos(id = "", widget = false))
                viewModel.listTraductions = mutableListOf()
                navController.navigate(ListScreensHome.home.route)
            },
            buttons = {
                TextButton(
                    onClick = {
                        viewModel.isSuccessFullOrError.postValue("")
                        viewModel.memoCreate.postValue(Memos(id = "", widget = false))
                        viewModel.listTraductions = (mutableListOf())
                        navController.navigate(ListScreensHome.home.route)
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
                Text(text = "Ocurrió un error al actualizar el memo", textAlign = TextAlign.Center)
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
    UpdateCardForm(viewModel, memo)
}

@Composable
fun UpdateCardForm(viewModel: HomeScreenViewModel, memo: Memos) {

    val isLoading: Boolean by viewModel.isLoadinCreateMemos.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        //ListLenguajes(viewModel, memo)
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
                    viewModel.updateMemoDb()
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
            Text(text = "Actualizar")
        }
    }
}