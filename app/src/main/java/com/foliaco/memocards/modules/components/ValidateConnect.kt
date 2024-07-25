package com.foliaco.memocards.modules.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foliaco.memocards.R
import kotlinx.coroutines.delay

@Composable
fun ValidateConnect() {
    val context = LocalContext.current
    var isConnect by remember { mutableStateOf(true) }
    val animationWidth by animateIntAsState(
        targetValue = if (isConnect) 0 else 275, label = "width animation"
    )
    val animationHeight by animateIntAsState(
        targetValue = if (isConnect) 0 else 60,
        label = "height animation"
    )

    LaunchedEffect(Unit) {
        delay(1000)
        isConnect = validateNetwork(context)
    }

    Row(
        modifier = Modifier
            .width(animationWidth.dp)
            .height(animationHeight.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color(0xFFecc8c5))
            .padding(vertical = 10.dp, horizontal = 20.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        Icon(
//            imageVector = Icons.Outlined.Warning,
//            contentDescription = "Warning Internet",
//            tint = Color(0xFFb63836)
//        )
        Text(
            text = "Sin conexión a internet",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color(0xFFb63836)
        )
        IconButton(
            onClick = {
                isConnect = validateNetwork(context)
            }, colors = IconButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xFFb63836),
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.Black
            )
        ) {
            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "Reload")

        }
    }
}


fun validateNetwork(context: Context): Boolean {
    val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilties = connectivity.activeNetwork ?: return false
        val networkInto = connectivity.getNetworkCapabilities(networkCapabilties) ?: return false

        return networkInto.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkInto.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    } else {
        val networkInfo = connectivity.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}