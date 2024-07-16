package com.foliaco.memocards.modules.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foliaco.memocards.modules.components.CardItemList
import com.foliaco.memocards.modules.components.SelectLenguaje
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.utils.bottomBorder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val auth = Firebase.auth
    val user = auth.currentUser
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val listLenguajes = viewModel.getLenguajes()
    val lenguajeSelected: String by viewModel.lenguajeSelect.observeAsState("")
    val memos: MutableList<Memos> by viewModel.memos.observeAsState(mutableListOf())

    LaunchedEffect(key1 = Unit) {
        viewModel.getCardByIdLenguaje()
    }

    if (user == null) {
        return CircularProgressIndicator()
    }
    Column {
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = "Bienvenido ${user!!.displayName ?: user!!.email}",
                modifier = Modifier.bottomBorder(3.dp, MaterialTheme.colorScheme.secondary)
            )
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .clickable {
                        viewModel.getLenguajes()
                        scope.launch {
                            sheetState.show()
                        }
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            ) {
                Text(
                    text = lenguajeSelected,
                    style = TextStyle(
                        fontSize = 14.sp,
                    )
                )
            }

            SelectLenguaje(
                sheetState = sheetState,
                scope = scope,
                listLenguajes = listLenguajes,
                onSelect = {
                    val item = listLenguajes[it]
                    if (item != null) {
                        viewModel.setLenguaje(item["title"] as String)
                    }
                }
            )
        }
        Column {
            Text(
                text = "Items ${memos?.size}",
                modifier = Modifier.padding(bottom = 5.dp, start = 15.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
            ) {
                items(memos.size) {
                    ListItem(
                        headlineContent = {
                            CardItemList(
                                modifier = Modifier,
                                memo = memos[it]
                            )
                        },
                        colors = ListItemColors(
                            containerColor = Color.Transparent,
                            headlineColor = Color.Transparent,
                            disabledHeadlineColor = Color.Transparent,
                            leadingIconColor = Color.Transparent,
                            overlineColor = Color.Transparent,
                            trailingIconColor = Color.Transparent,
                            supportingTextColor = Color.Transparent,
                            disabledLeadingIconColor = Color.Transparent,
                            disabledTrailingIconColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                    )
                }
            }
        }
    }
}