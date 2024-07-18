package com.foliaco.memocards.modules.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foliaco.memocards.modules.home.model.Memos
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.ColorText
import com.foliaco.memocards.modules.theme.ColorText2
import com.foliaco.memocards.modules.theme.DisableButton
import com.foliaco.memocards.modules.theme.DisableText
import com.foliaco.memocards.modules.theme.Easy
import com.foliaco.memocards.modules.theme.Hard
import com.foliaco.memocards.modules.theme.MemoCardsTheme
import com.foliaco.memocards.modules.theme.Middle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CardItemList(modifier: Modifier, memo: Memos, viewModel: HomeScreenViewModel) {

    MemoCardsTheme {
        Row(modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { }
            .clip(shape = RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 10.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .width(150.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = memo.key.toString(),
                    color = ColorText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
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
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val nivel = "easy"
                Button(
                    onClick = {
                    },
                    colors = ButtonColors(
                        disabledContainerColor = DisableButton,
                        contentColor = ColorText,
                        disabledContentColor = DisableText,
                        containerColor = when (nivel) {
                            "easy" -> Easy
                            "middle" -> Middle
                            "hard" -> Hard
                            else -> Easy
                        }
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 10.dp),
                    modifier = modifier
                        .wrapContentHeight()
                        .padding(0.dp)
                        .height(30.dp)
                ) {
                    Text(
                        text = nivel, fontSize = 16.sp, color = ColorText
                    )
                }
                Button(
                    onClick = {
                        if (memo.id != null) {
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
        }
    }
}