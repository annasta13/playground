package com.annas.playground.kotlin.ui.screen.documentScanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.BodyText
import com.annas.playground.kotlin.ui.components.PrimaryButton
import com.annas.playground.kotlin.ui.components.SecondaryButton


/**
 * Created by Annas Surdyanto on 02/03/2023
 * @param onRetake is the unit triggered when the user click the retake button to re-open the camera.
 */
@Composable
fun ScanResultView(
    modifier: Modifier = Modifier,
    text: String,
    onRetake: () -> Unit
) {
    val context = LocalContext.current
    ConstraintLayout(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val (textRef, buttonRef) = createRefs()
        Column(
            modifier = Modifier.constrainAs(textRef) {
                top.linkTo(parent.top)
                bottom.linkTo(buttonRef.top, margin = 8.dp)
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BodyText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.scan_result),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            BodyText(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonRef) {
                    bottom.linkTo(parent.bottom)
                },
            ) {
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.retake),
                onClick = onRetake
            )
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.copy_text),
                onClick = {
                    (context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?)?.apply {
                        val clip = ClipData.newPlainText("Copied Text", text)
                        setPrimaryClip(clip)
                    }
                }
            )
        }
    }
}
