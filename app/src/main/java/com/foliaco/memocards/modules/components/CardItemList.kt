package com.foliaco.memocards.modules.components


import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.ColorText
import com.foliaco.memocards.modules.theme.ColorText2
import com.foliaco.memocards.modules.theme.DisableButton
import com.foliaco.memocards.modules.theme.DisableText
import com.foliaco.memocards.modules.theme.Hard
import com.foliaco.memocards.modules.theme.Middle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardItemList(modifier: Modifier, memo: Memos, viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    var openOptions by remember { mutableStateOf(false) }
    val size by animateDpAsState(if (openOptions) 120.dp else 0.dp, label = "DpAnimate")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .combinedClickable(
                enabled = true,
                onClick = {},
                onLongClick = {
                    println("memo.made_in ${memo.made_in}")
                    if (memo.made_in == "admin") {
                        Toast
                            .makeText(
                                context,
                                "Solo puedes eliminar tus tarjetas",
                                Toast.LENGTH_SHORT,
                            )
                            .show()
                    } else {
                        openOptions = !openOptions
                    }
                },

                ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = memo.key.toString(),
                color = ColorText,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (memo.reading_on != "null" && memo.reading_on != "") {
                println("Hola Error ${memo.reading_on}")
                Text(
                    text = memo.reading_on.toString(),
                    color = ColorText2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            if (memo.reading_kun != "null" && memo.reading_kun != "") {
                Text(
                    text = memo.reading_kun.toString(),
                    color = ColorText2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            if (memo.reading != "null" && (memo.reading_on == "null" || memo.reading_on == "") && (memo.reading_kun == "null" || memo.reading_kun == "")) {
                Text(
                    text = memo.reading.toString(),
                    color = ColorText2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text(
                text = memo.value.toString(),
                color = ColorText2,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Row(
            modifier = modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    if (memo.id != "") {
                        memo.lenguajeId = viewModel.lenguajeIdSelect.value
                        viewModel.enableOrDisableWidget(memo = memo, idMemo = memo.id)
                    }
                },
                colors = ButtonColors(
                    disabledContainerColor = DisableButton,
                    contentColor = if (memo.widget == true) ColorText else DisableText,
                    disabledContentColor = DisableText,
                    containerColor = if (memo.widget == true) MaterialTheme.colorScheme.secondary else DisableButton
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 10.dp),
                modifier = modifier
                    .wrapContentHeight()
                    .padding(0.dp)
                    .height(30.dp)
            ) {
                Text(
                    text = "Widget",
                    fontSize = 16.sp,
                )
            }
        }
        if (openOptions) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(size)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.Transparent,
                        containerColor = Middle,
                        disabledContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Text(text = "Editar")
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.Transparent,
                        containerColor = Hard,
                        disabledContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Text(text = "Eliminar")
                }
            }
        }
    }
}