package com.armsx2.ui.about

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.armsx2.i18n.str
import com.armsx2.ui.common.ArmsBackdrop
import com.armsx2.ui.common.ArmsLogo
import com.armsx2.ui.common.ArmsTopBar
import com.armsx2.ui.common.GlassPanel
import com.armsx2.ui.common.RoundAction
import com.armsx2.ui.settings.ControllerAutoScroll
import com.armsx2.ui.settings.LocalSettingsScrollState
import com.armsx2.ui.settings.controllerFocusable

private const val RepositoryUrl = "https://github.com/ARMSX2/ARMSX2"
// Meu app: onde fica o codigo-fonte desta versao (exigencia da GPL-3).
private const val SourceUrl = "https://github.com/Uanderson11/ARMSX2/tree/meu-app"
private const val Pcsx2RepositoryUrl = "https://github.com/PCSX2/pcsx2"

@Composable
fun AboutScreen(onBack: () -> Unit, viewModel: AboutViewModel = viewModel()) {
    val state = viewModel.state.value
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(Unit) { viewModel.load() }
    var showLicense by remember { mutableStateOf(false) }
    var showOpenSource by remember { mutableStateOf(false) }
    // The cards are controller rows, so the page scrolls to whichever one is selected.
    val scroll = rememberScrollState()
    ControllerAutoScroll(scroll)

    ArmsBackdrop {
        CompositionLocalProvider(LocalSettingsScrollState provides scroll) {
            Column(Modifier.fillMaxSize().verticalScroll(scroll)) {
                ArmsTopBar(
                    title = str("about.title"),
                    leading = { RoundAction("←", str("action.back"), onBack) },
                )
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val compact = maxWidth < 760.dp
                    val overview: @Composable (Modifier) -> Unit = { panelModifier ->
                        GlassPanel(panelModifier) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Meu app: icone e nome do Retro Player no lugar do logo do ARMSX2.
                                androidx.compose.foundation.Image(
                                    painter = androidx.compose.ui.res.painterResource(com.armsx2.R.drawable.ic_launcher_foreground),
                                    contentDescription = "Retro Player",
                                    modifier = Modifier
                                        .size(132.dp)
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(androidx.compose.ui.graphics.Color(0xFF0E1014)),
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "Retro Player",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                    val information: @Composable (Modifier) -> Unit = { panelModifier ->
                        GlassPanel(panelModifier) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                PanelTitle(str("about.build.title"))
                                InfoRow(str("about.appVersion"), state.appVersion)
                                InfoRow(str("about.coreVersion"), state.coreVersion)
                            }
                        }
                    }
                    val hardware: @Composable (Modifier) -> Unit = { panelModifier ->
                        GlassPanel(panelModifier) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                PanelTitle(str("about.hardware.title"))
                                InfoRow(str("about.device"), state.device)
                                InfoRow(str("about.androidVersion"), state.androidVersion)
                                InfoRow(str("about.soc"), state.soc)
                                InfoRow(str("about.gpu"), state.gpu)
                                InfoRow(str("about.cpuCores"), state.cpu)
                                InfoRow(str("about.memory"), state.memory)
                                InfoRow(str("about.display"), state.display)
                                InfoRow(str("about.architecture"), state.architecture)
                                InfoRow(str("about.pageSize"), state.pageSize)
                            }
                        }
                    }
                    Column(
                        Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (compact) {
                            overview(Modifier.fillMaxWidth())
                            information(Modifier.fillMaxWidth())
                            hardware(Modifier.fillMaxWidth())
                        } else {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                overview(Modifier.weight(1f))
                                information(Modifier.weight(1f))
                            }
                            hardware(Modifier.fillMaxWidth())
                        }
                        // Meu app: creditos e avisos de licenca num rodape minusculo. A GPL-3 exige que
                        // o app mostre copyright, ausencia de garantia e onde ler a licenca (secao 5d),
                        // entao eles ficam aqui, discretos, mas presentes e tocaveis.
                        Spacer(Modifier.height(28.dp))
                        val tiny = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                        Column(
                            Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                "Baseado em PCSX2 e ARMSX2. $ArmsCopyright. Sem garantia. Licença GPL-3.",
                                fontSize = 9.sp, color = tiny, lineHeight = 11.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Licença", fontSize = 9.sp, color = tiny,
                                    modifier = Modifier.clickable { showLicense = true })
                                Text("Código aberto", fontSize = 9.sp, color = tiny,
                                    modifier = Modifier.clickable { showOpenSource = true })
                                Text("Código-fonte", fontSize = 9.sp, color = tiny,
                                    modifier = Modifier.clickable { uriHandler.openUri(SourceUrl) })
                            }
                            Text(
                                listOf(
                                    str("app.credits.music"), str("app.credits.museumMusic"),
                                    str("app.credits.texturesMusic"), str("app.credits.sfx"),
                                ).joinToString("  "),
                                fontSize = 8.sp, color = tiny, lineHeight = 10.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
    if (showLicense) GplPanel(onClose = { showLicense = false })
    if (showOpenSource) OpenSourceLicensesPanel(onClose = { showOpenSource = false })
}

@Composable
private fun ProjectCard(
    title: String,
    repository: String,
    description: String,
    glyph: String,
    modifier: Modifier = Modifier,
    secondary: Boolean = false,
    controllerId: String,
    // ↗ leaves the app (a link); › opens a panel in it.
    trailing: String = "↗",
    onOpen: () -> Unit,
) {
    val accent = if (secondary) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(20.dp)
    Surface(
        onClick = onOpen,
        modifier = modifier.fillMaxWidth().controllerFocusable(controllerId, shape, onConfirm = onOpen),
        shape = shape,
        color = accent.copy(alpha = 0.10f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.46f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = accent.copy(alpha = 0.14f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(glyph, color = accent, fontSize = if (glyph.length > 2) 13.sp else 22.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.size(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    repository,
                    style = MaterialTheme.typography.bodyMedium,
                    color = accent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(trailing, color = accent, fontSize = 22.sp)
        }
    }
}

@Composable
private fun PanelTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            value.ifBlank { "…" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.15f),
        )
    }
}
