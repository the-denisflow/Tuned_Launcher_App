package com.example.tuned_launcher_app.presentation.mainpage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.tuned_launcher_app.domain.model.AppInfo


@Composable
fun LauncherAppGrid(modifier: Modifier = Modifier, apps: List<AppInfo>) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(AppGridDefaults.GRID_COLUMNS),
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(AppGridDefaults.GRID_CONTENT_PADDING),
            horizontalArrangement = Arrangement.spacedBy(AppGridDefaults.GRID_HORIZONTAL_SPACING),
            verticalArrangement = Arrangement.spacedBy(AppGridDefaults.GRID_VERTICAL_SPACING),
        ) {
            items(items = apps, key = { it.packageName }) { app ->
                AppGridUiItem(appInfo = app)
            }
        }
    }
}

@Composable
fun AppGridUiItem(appInfo: AppInfo) {
    val iconBitmap = remember(appInfo.packageName) { appInfo.icon.toBitmap() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppGridDefaults.ICON_LABEL_SPACING),
    ) {
        Image(
            bitmap = iconBitmap.asImageBitmap(),
            contentDescription = appInfo.label,
            modifier = Modifier.size(AppGridDefaults.ICON_SIZE)
        )
        Text(
            text = appInfo.label,
            color = Color.Black,
            fontSize = AppGridDefaults.LABEL_FONT_SIZE.sp,
            maxLines = AppGridDefaults.LABEL_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}