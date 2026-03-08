package com.example.tuned_launcher_app.presentation.mainpage.components

import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuned_launcher_app.domain.model.AppInfo

private fun mockAppInfo(label: String, color: Int) = AppInfo(
    label = label,
    packageName = "com.example.$label",
    componentName = ComponentName("com.example.$label", "MainActivity"),
    icon = ColorDrawable(color),
    launchIntent = Intent(),
)

@Preview(showBackground = true)
@Composable
private fun LauncherAppGridPreview() {
    val fakeApps = listOf(
        mockAppInfo("Chrome", android.graphics.Color.BLUE),
        mockAppInfo("Maps", android.graphics.Color.GREEN),
        mockAppInfo("Camera", android.graphics.Color.RED),
        mockAppInfo("Settings", android.graphics.Color.GRAY),
        mockAppInfo("Music", android.graphics.Color.MAGENTA),
        mockAppInfo("Calendar", android.graphics.Color.CYAN),
        mockAppInfo("Photos", android.graphics.Color.YELLOW),
        mockAppInfo("Messages", android.graphics.Color.DKGRAY),
    )
    LauncherAppGrid(apps = fakeApps)
}

@Preview(showBackground = true)
@Composable
private fun AppGridUiItemPreview() {
    AppGridUiItem(appInfo = mockAppInfo("YouTube", android.graphics.Color.RED))
}
