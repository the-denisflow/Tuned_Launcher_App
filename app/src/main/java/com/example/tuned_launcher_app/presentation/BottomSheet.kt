package com.example.tuned_launcher_app.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.tuned_launcher_app.presentation.mainpage.components.scoped.manageHorizontalGesture
import com.example.tuned_launcher_app.presentation.mainpage.components.scoped.rememberHorizontalGestureHandler
import kotlin.Float

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun BottomSheet() {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val offsetAnimatable = remember { Animatable(0f) }
    val screenWidthPx = remember (configuration, density) {
        with(density) {
            configuration.screenWidthDp.dp.toPx()
        }
    }
    val horizontalGestureHandler = rememberHorizontalGestureHandler(
        scope,
        density,
        offsetAnimatable,
        screenWidthPx
    )

    Box(
        modifier = Modifier.fillMaxSize()
    )  {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter)
                .height(50.dp).width(200.dp)
                .manageHorizontalGesture(
                    enabled =  true,
                    handler = horizontalGestureHandler
                )
                .background(Color.DarkGray)

        ) {
            Text("Hello in my App")
        }
    }
}