package com.annas.playground.ui.screen.documentScanner

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.annas.playground.R
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.utils.ContextExtension.openSetting
import com.annas.playground.utils.ContextExtension.toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS

@Composable
fun DocumentScannerScreen(navigateUp: () -> Unit) {
    val context = LocalContext.current

    /** Scanned text value*/
    var scannedText by remember { mutableStateOf<String?>(null) }

    /** Text recognizer to operate scanning*/
    val textRecognizer = remember { TextRecognition.getClient(DEFAULT_OPTIONS) }

    /** Unit to handle when failure occurs*/
    val onFailure: (Throwable) -> Unit = { context.toast(it.message.toString()) }

    var isPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        if (!isGranted) {
            context.toast(context.getString(R.string.camera_permission_denied))
            navigateUp()
        } else isPermissionGranted = true
    }

    LaunchedEffect(Unit) {
        if (!isCameraPermissionGranted(context)) permissionLauncher.launch(CAMERA)
        else isPermissionGranted = true
    }

    val mlkitLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultScanner =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                resultScanner?.pages?.let { pages ->
                    pages.map { page -> page.imageUri }.firstOrNull()?.let {
                        val image = InputImage.fromFilePath(context, it)
                        textRecognizer.process(image)
                            .addOnSuccessListener { scannedText = it.text }
                            .addOnFailureListener(onFailure)
                    }
                }
            }
        }

    val scanner by remember {
        lazy {
            val options = GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setPageLimit(1)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                .build()
            GmsDocumentScanning.getClient(options)
        }
    }

    val openObjectScanner: () -> Unit = {
        runCatching {
            scanner.getStartScanIntent(context as Activity)
                .addOnSuccessListener { intentSender ->
                    mlkitLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                }
                .addOnFailureListener(onFailure)
        }.onFailure(onFailure)
    }

    /** Unit to start camera*/
    val launchCamera: () -> Unit = {
        if (isCameraPermissionGranted(context)) openObjectScanner()
        else context.openSetting()
    }

    ScreenContainer(barTitle = stringResource(id = R.string.scan_image_text)) {
        scannedText?.let {
            ScanResultView(
                modifier = Modifier,
                text = it,
                onRetake = launchCamera
            )
        }
        AnimatedVisibility(scannedText == null && isPermissionGranted) {
            StandbyView(
                modifier = Modifier,
                onTakePicture = launchCamera
            )
        }
    }
}

fun isCameraPermissionGranted(context: Context) =
    ContextCompat.checkSelfPermission(context, CAMERA) == PERMISSION_GRANTED
