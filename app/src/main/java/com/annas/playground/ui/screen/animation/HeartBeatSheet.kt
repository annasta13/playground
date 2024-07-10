package com.annas.playground.ui.screen.animation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.ui.components.Space
import com.annas.playground.ui.components.TitleText
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.MediumPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartBeatSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit,
    onSelected: (Int) -> Unit
) {
    ModalBottomSheet(
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        onDismissRequest = onDismiss,
    ) {
        HeartBeatSheetContent(onSelected, onDismiss)
    }
}

@Composable
private fun HeartBeatSheetContent(
    onSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val list = remember { mutableStateListOf(40, 50, 60, 70, 80, 90, 100, 110, 120) }
    Column(
        modifier = Modifier.padding(LargePadding),
        verticalArrangement = Arrangement.spacedBy(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = stringResource(id = R.string.select_heart_beat))
        Space()
        list.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelected(it).also { onDismiss() } },
                colors = CardDefaults.cardColors()
                    .copy(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                TitleText(
                    modifier = Modifier
                        .padding(MediumPadding)
                        .align(Alignment.CenterHorizontally),
                    text = it.toString()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeartBeatSheetPreview() {
    HeartBeatSheetContent({}, {})
}
