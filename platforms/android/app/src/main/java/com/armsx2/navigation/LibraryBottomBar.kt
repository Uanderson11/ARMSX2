package com.armsx2.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Meu app: barra de navegacao inferior da biblioteca (layout novo). */
private enum class BottomTab(val label: String) {
    Library("Biblioteca"),
    Saves("Saves"),
    Achievements("Conquistas"),
    Settings("Ajustes"),
}

@Composable
fun LibraryBottomBar(modifier: Modifier = Modifier) {
    val outline = MaterialTheme.colorScheme.outline
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(outline))
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            BottomTab.values().forEach { tab ->
                val active = tab == BottomTab.Library
                val color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                Column(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(role = Role.Tab) {
                            when (tab) {
                                BottomTab.Library -> Unit
                                BottomTab.Saves -> UiNavigator.navigate(AppRoute.SaveManager)
                                BottomTab.Achievements -> UiNavigator.navigate(AppRoute.Achievements)
                                BottomTab.Settings -> UiNavigator.navigate(AppRoute.Settings())
                            }
                        }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Canvas(Modifier.size(22.dp)) { drawTabIcon(tab, color) }
                    Text(
                        tab.label,
                        color = color,
                        fontSize = 12.sp,
                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawTabIcon(tab: BottomTab, color: Color) {
    val u = size.minDimension / 24f
    val stroke = Stroke(width = 2f * u, cap = StrokeCap.Round)
    fun rr(x: Float, y: Float, w: Float, h: Float) = drawRoundRect(
        color, Offset(x * u, y * u), Size(w * u, h * u), CornerRadius(1.5f * u), style = stroke,
    )
    when (tab) {
        BottomTab.Library -> {
            rr(3f, 3f, 7f, 9f); rr(14f, 3f, 7f, 9f); rr(3f, 15f, 7f, 6f); rr(14f, 15f, 7f, 6f)
        }
        BottomTab.Saves -> {
            val p = Path().apply {
                moveTo(5f * u, 3f * u); lineTo(16f * u, 3f * u); lineTo(19f * u, 6f * u)
                lineTo(19f * u, 21f * u); lineTo(5f * u, 21f * u); close()
            }
            drawPath(p, color, style = stroke)
            rr(8f, 13f, 8f, 5f)
            drawLine(color, Offset(8f * u, 3f * u), Offset(8f * u, 8f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(8f * u, 8f * u), Offset(15f * u, 8f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(15f * u, 8f * u), Offset(15f * u, 3f * u), 2f * u, StrokeCap.Round)
        }
        BottomTab.Achievements -> {
            val cup = Path().apply {
                moveTo(8f * u, 4f * u); lineTo(16f * u, 4f * u); lineTo(16f * u, 9f * u)
                cubicTo(16f * u, 14.3f * u, 8f * u, 14.3f * u, 8f * u, 9f * u); close()
            }
            drawPath(cup, color, style = stroke)
            drawLine(color, Offset(12f * u, 13f * u), Offset(12f * u, 17f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(9f * u, 20f * u), Offset(15f * u, 20f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(8f * u, 6f * u), Offset(5f * u, 6f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(16f * u, 6f * u), Offset(19f * u, 6f * u), 2f * u, StrokeCap.Round)
        }
        BottomTab.Settings -> {
            drawLine(color, Offset(4f * u, 7f * u), Offset(14f * u, 7f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(18f * u, 7f * u), Offset(20f * u, 7f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(4f * u, 17f * u), Offset(8f * u, 17f * u), 2f * u, StrokeCap.Round)
            drawLine(color, Offset(12f * u, 17f * u), Offset(20f * u, 17f * u), 2f * u, StrokeCap.Round)
            drawCircle(color, 2f * u, Offset(16f * u, 7f * u), style = stroke)
            drawCircle(color, 2f * u, Offset(10f * u, 17f * u), style = stroke)
        }
    }
}
