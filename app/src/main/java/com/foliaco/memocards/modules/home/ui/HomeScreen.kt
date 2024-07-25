package com.foliaco.memocards.modules.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.foliaco.memocards.modules.components.CardItemList
import com.foliaco.memocards.modules.components.SelectLenguaje
import com.foliaco.memocards.modules.economia.model.CoinManager
import com.foliaco.memocards.modules.home.model.FirebaseModel
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.utils.bottomBorder
import com.foliaco.memocards.utils.constant
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavHostController
) {
    val auth = Firebase.auth
    val user = auth.currentUser
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val listLenguajes = viewModel.getLenguajes()
    val lenguajeSelected: String by viewModel.lenguajeSelect.observeAsState("")
    val lenguajeIdSelected: String by viewModel.lenguajeIdSelect.observeAsState("")
    val isLoadingMemos: Boolean by viewModel.isLoadingMemos.observeAsState(initial = true)
    val memos: MutableList<Memos> by viewModel.memos.observeAsState(mutableListOf())

    LaunchedEffect(key1 = Unit) {
        viewModel.getCoinByUser()
        viewModel.getCardByIdLenguaje(lenguajeIdSelected)
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
            Column(modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(3.dp))
                .clickable {
                    viewModel.getLenguajes()
                    scope.launch {
                        sheetState.show()
                    }
                }
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)) {
                Text(
                    text = lenguajeSelected, style = TextStyle(
                        fontSize = 14.sp,
                    )
                )
            }

            SelectLenguaje(sheetState = sheetState,
                scope = scope,
                listLenguajes = listLenguajes,
                onSelect = { name, id ->
                    viewModel.setLenguaje(
                        name, id
                    )
                    viewModel.getCardByIdLenguaje(id)
                    scope.launch {
                        sheetState.hide()
                    }
                })
        }
        Column {
            Text(
                text = "Items ${memos?.size}",
                modifier = Modifier.padding(bottom = 5.dp, start = 15.dp)
            )
            if (isLoadingMemos) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        trackColor = MaterialTheme.colorScheme.secondary,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                ) {
                    items(memos.size) {
                        ListItem(
                            headlineContent = {
                                Column {

                                    CardItemList(
                                        modifier = Modifier,
                                        memo = memos[it],
                                        viewModel = viewModel,
                                        navController = navController
                                    )
                                    if (it % 3 == 0 && user!!.email.orEmpty() != "foliaco18@gmail.com") {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp, bottom = 5.dp)
                                        ) {
                                            AddsBanner()
                                        }
                                    }
                                }
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
}


@Composable
fun AddsBanner() {
    val adWidth = LocalConfiguration.current.screenWidthDp - 32
    AndroidView(factory = { context ->
        val adView = AdView(context)
        adView.setAdSize(AdSize(adWidth, 132))
        adView.apply {
            adUnitId = constant.app_banner_home_id
            loadAd(AdRequest.Builder().build())
        }
    })
}