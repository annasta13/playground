package com.annas.playground.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.annas.playground.ui.components.zoomableimage.TouchImageView
import com.annas.playground.ui.theme.MediumPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    title: String,
    contentDescription: String? = null,
    @DrawableRes resource: Int
) {
    val showImage = remember { mutableStateOf(false) }
    Image(
        painter = painterResource(id = resource),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.clickable { showImage.value = true }
    )

    ZoomableImage(
        resource = resource,
        isShowing = showImage,
        title = title
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomableImage(
    @DrawableRes resource: Int,
    title: String,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    isShowing: MutableState<Boolean>
) {
    val onDismiss: () -> Unit = { isShowing.value = false }
    if (isShowing.value) ModalBottomSheet(
        shape = RectangleShape,
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        onDismissRequest = onDismiss,
        dragHandle = null,
    ) {
        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(MediumPadding), factory = {
                val imageView = TouchImageView(context)
                imageView.setImageResource(resource)
                imageView
            })

            BodyText(
                text = title, modifier = Modifier
                    .padding(MediumPadding)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(bottom = MediumPadding)
            )
        }
    }
}
