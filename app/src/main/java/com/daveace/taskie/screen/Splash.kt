package com.daveace.taskie.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daveace.taskie.R
import com.daveace.taskie.ui.theme.dark
import com.daveace.taskie.ui.theme.light

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    val splashShape = GenericShape { size, _ ->
        size.apply {
            moveTo(width, 0F)
            lineTo(width, height)
            lineTo(0F, height)
            close()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = dark, shape = splashShape)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier.align(alignment = Alignment.Center)
                .background(color=dark)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.taskie),
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive,
                color = light
            )
        }
    }
}