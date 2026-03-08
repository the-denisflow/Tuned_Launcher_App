package com.example.tuned_launcher_app.domain.model

import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfo(
    val label: String,
    val packageName: String,
    val componentName: ComponentName,
    val icon: Drawable,
    val launchIntent: Intent,
)