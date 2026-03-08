package com.example.tuned_launcher_app.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Process
import com.example.tuned_launcher_app.domain.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppRepository(private val context: Context) {

    companion object {
        private const val ICON_DENSITY = 0
    }

    private fun getAllApps(launcherApps: LauncherApps): List<AppInfo> =
        launcherApps.getActivityList(null, Process.myUserHandle()).filter {
            it.applicationInfo.packageName != context.packageName }
            .map { activityInfo ->
                AppInfo(
                    label = activityInfo.label.toString(),
                    packageName = activityInfo.componentName.packageName,
                    componentName = activityInfo.componentName,
                    icon = activityInfo.getIcon(ICON_DENSITY),
                    launchIntent = Intent(Intent.ACTION_MAIN).apply {
                        component = activityInfo.componentName
                        addCategory(Intent.CATEGORY_LAUNCHER)  } )
        }

    fun getApps(): Flow<List<AppInfo>> = flow {
        val launcherApps = context.getSystemService(LauncherApps::class.java)
        if (launcherApps == null) {
            emit(emptyList())
            return@flow
        }
        emit(
            getAllApps(launcherApps)
        )
    }.flowOn(Dispatchers.IO)
}