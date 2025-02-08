package com.annas.playground.kotlin.ui.screen.objectdetector

import android.Manifest.permission.CAMERA
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.EmptyView
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.screen.documentScanner.isCameraPermissionGranted
import com.annas.playground.kotlin.utils.ContextExtension.openSetting
import com.annas.playground.kotlin.utils.ContextExtension.toast

@Composable
fun DetectorContainer(
    onNavigateUp: () -> Unit,
    title: String,
    cameraView: @Composable () -> Unit
) {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }
    var showScanner by remember { mutableStateOf(true) }

    /** Unit to start camera*/
    val launchCamera: () -> Unit = {
        if (isCameraPermissionGranted(context)) showScanner = true
        else context.openSetting()
    }
    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        if (!isGranted) {
            context.toast(context.getString(R.string.camera_permission_denied))
            onNavigateUp()
        } else {
            isPermissionGranted = true
            launchCamera()
        }
    }

    LaunchedEffect(Unit) {
        if (!isCameraPermissionGranted(context)) permissionLauncher.launch(CAMERA)
        else {
            isPermissionGranted = true
            launchCamera()
        }
    }


    ScreenContainer(barTitle = title, onLeadingIconClicked = onNavigateUp) {
        if (showScanner) cameraView()
        else EmptyView(message = stringResource(R.string.app_not_ready))
    }
}
