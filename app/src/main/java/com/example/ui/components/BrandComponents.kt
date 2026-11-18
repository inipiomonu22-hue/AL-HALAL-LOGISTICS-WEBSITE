package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.BusinessSettingsEntity
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.ui.theme.WhatsAppGreen
import kotlin.math.cos
import kotlin.math.sin

val DesktopBreakpoint = 1200.dp
val TabletBreakpoint = 768.dp

@Composable
fun ResponsiveContainer(
    modifier: Modifier = Modifier,
    maxContentWidth: Dp = 1320.dp,
    verticalPadding: Dp = 0.dp,
    content: @Composable BoxWithConstraintsScope.(isDesktop: Boolean, isTablet: Boolean) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val isDesktop = maxWidth >= DesktopBreakpoint
        val isTablet = maxWidth >= TabletBreakpoint && maxWidth < DesktopBreakpoint
        val horizontalPad = when {
            isDesktop -> 36.dp
            isTablet -> 24.dp
            else -> 16.dp
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxContentWidth)
                .padding(horizontal = horizontalPad, vertical = verticalPadding)
        ) {
            this@BoxWithConstraints.content(isDesktop, isTablet)
        }
    }
}

@Composable
fun <T> ResponsiveGrid(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 16.dp,
    verticalGap: Dp = 16.dp,
    itemContent: @Composable (T) -> Unit
) {
    val safeCols = columns.coerceAtLeast(1)
    if (safeCols == 1) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(verticalGap)
        ) {
            items.forEach { item ->
                itemContent(item)
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(verticalGap)
        ) {
            items.chunked(safeCols).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(horizontalGap)
                ) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            itemContent(item)
                        }
                    }
                    repeat(safeCols - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun IslamicGeometricBackground(
    modifier: Modifier = Modifier,
    lineColor: Color = GoldRich.copy(alpha = 0.14f)
) {
    Canvas(modifier = modifier) {
        val cellSize = 68.dp.toPx()
        val cols = (size.width / cellSize).toInt() + 2
        val rows = (size.height / cellSize).toInt() + 2
        val radius = cellSize * 0.34f
        val stroke = Stroke(width = 1.dp.toPx())

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cx = c * cellSize + if (r % 2 == 1) cellSize * 0.5f else 0f
                val cy = r * cellSize
                val path = Path()
                for (i in 0 until 4) {
                    val angle = Math.toRadians((i * 90).toDouble())
                    val x = cx + radius * cos(angle).toFloat()
                    val y = cy + radius * sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, color = lineColor, style = stroke)

                val pathRotated = Path()
                for (i in 0 until 4) {
                    val angle = Math.toRadians((i * 90 + 45).toDouble())
                    val x = cx + radius * cos(angle).toFloat()
                    val y = cy + radius * sin(angle).toFloat()
                    if (i == 0) pathRotated.moveTo(x, y) else pathRotated.lineTo(x, y)
                }
                pathRotated.close()
                drawPath(pathRotated, color = lineColor, style = stroke)

                drawCircle(
                    color = lineColor.copy(alpha = lineColor.alpha * 0.7f),
                    radius = radius * 0.22f,
                    center = Offset(cx, cy),
                    style = stroke
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlHalalCorporateDesktopHeader(
    currentRoute: String,
    whatsappDisplay: String,
    isDesktop: Boolean,
    onNavigate: (String) -> Unit,
    onRequestQuote: () -> Unit,
    onOpenWhatsApp: () -> Unit
) {
    var logisticsMenuOpen by remember { mutableStateOf(false) }
    var suppliesMenuOpen by remember { mutableStateOf(false) }

    Surface(
        color = EmeraldDeep,
        contentColor = PureWhite,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("corporate_desktop_header")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isDesktop) 36.dp else 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                    // LEFT: Logo & Corporate Branding
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onNavigate(AppRoutes.ROUTE_HOME) }
                            .padding(end = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_art),
                            contentDescription = "AL-HALAL LOGISTICS N TRANS logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "AL-HALAL LOGISTICS N TRANS",
                                style = MaterialTheme.typography.titleMedium,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "INTERNATIONAL FREIGHT & ISLAMIC SUPPLIES",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldBright,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // CENTER: Horizontal Navigation Menu with Dropdowns
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(if (isDesktop) 6.dp else 2.dp),
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        DesktopNavLink(
                            label = "HOME",
                            selected = currentRoute == AppRoutes.ROUTE_HOME,
                            onClick = { onNavigate(AppRoutes.ROUTE_HOME) }
                        )
                        DesktopNavLink(
                            label = "ABOUT US",
                            selected = currentRoute == AppRoutes.ROUTE_ABOUT,
                            onClick = { onNavigate(AppRoutes.ROUTE_ABOUT) }
                        )
                        Box {
                            DesktopNavDropdownLink(
                                label = "LOGISTICS SERVICES",
                                selected = currentRoute == AppRoutes.ROUTE_LOGISTICS || currentRoute.startsWith("service_"),
                                onClick = { logisticsMenuOpen = true }
                            )
                            DropdownMenu(
                                expanded = logisticsMenuOpen,
                                onDismissRequest = { logisticsMenuOpen = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All Logistics Services", fontWeight = FontWeight.SemiBold) },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_LOGISTICS)
                                    },
                                    modifier = Modifier.testTag("desktop_dropdown_all_logistics")
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("International Air Freight") },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_AIR_FREIGHT)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("International Sea Freight") },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_SEA_FREIGHT)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("South African Customs Clearance") },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_CUSTOMS_CLEARANCE)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Global Door-to-Door Delivery") },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_DOOR_TO_DOOR)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("SADC Road Freight") },
                                    onClick = {
                                        logisticsMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_SADC_ROAD_FREIGHT)
                                    }
                                )
                            }
                        }
                        Box {
                            DesktopNavDropdownLink(
                                label = "ISLAMIC SUPPLIES",
                                selected = currentRoute.startsWith("islamic_"),
                                onClick = { suppliesMenuOpen = true }
                            )
                            DropdownMenu(
                                expanded = suppliesMenuOpen,
                                onDismissRequest = { suppliesMenuOpen = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Islamic Supplies Overview", fontWeight = FontWeight.SemiBold) },
                                    onClick = {
                                        suppliesMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Islamic Men's Clothing") },
                                    onClick = {
                                        suppliesMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_MENS_CLOTHING)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Islamic Women's Clothing") },
                                    onClick = {
                                        suppliesMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_WOMENS_CLOTHING)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Islamic Accessories & Products") },
                                    onClick = {
                                        suppliesMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Islamic Product Guidance") },
                                    onClick = {
                                        suppliesMenuOpen = false
                                        onNavigate(AppRoutes.ROUTE_ISLAMIC_GUIDANCE)
                                    }
                                )
                            }
                        }
                        DesktopNavLink(
                            label = "REQUEST A QUOTE",
                            selected = currentRoute == AppRoutes.ROUTE_QUOTE,
                            onClick = onRequestQuote
                        )
                        DesktopNavLink(
                            label = "CONTACT US",
                            selected = currentRoute == AppRoutes.ROUTE_CONTACT,
                            onClick = { onNavigate(AppRoutes.ROUTE_CONTACT) }
                        )
                    }

                    // RIGHT: Prominent WhatsApp Button (+27 74 076 7068)
                    Button(
                        onClick = onOpenWhatsApp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .height(44.dp)
                            .testTag("desktop_header_whatsapp_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = whatsappDisplay,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
            }
            HorizontalDivider(thickness = 1.5.dp, color = GoldRich.copy(alpha = 0.65f))
        }
    }
}

@Composable
private fun DesktopNavLink(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (selected) GoldRich.copy(alpha = 0.18f) else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) GoldBright else PureWhite.copy(alpha = 0.92f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun DesktopNavDropdownLink(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (selected) GoldRich.copy(alpha = 0.18f) else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, end = 6.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) GoldBright else PureWhite.copy(alpha = 0.92f),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = if (selected) GoldBright else GoldRich,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlHalalTopBar(
    title: String,
    onOpenMenu: () -> Unit,
    onRequestQuote: () -> Unit,
    onLogoClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = EmeraldDeep,
            titleContentColor = PureWhite,
            navigationIconContentColor = GoldBright,
            actionIconContentColor = GoldBright
        ),
        navigationIcon = {
            IconButton(
                onClick = onOpenMenu,
                modifier = Modifier.testTag("nav_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open navigation menu"
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = onLogoClick)
                    .padding(vertical = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_art),
                    contentDescription = "AL-HALAL LOGISTICS N TRANS logo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = PureWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "AL-HALAL LOGISTICS N TRANS",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldBright,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            Button(
                onClick = onRequestQuote,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldRich,
                    contentColor = CharcoalInk
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .testTag("top_bar_quote_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RequestQuote,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "QUOTE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun FloatingWhatsAppFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = WhatsAppGreen,
        contentColor = PureWhite,
        shape = CircleShape,
        modifier = modifier.testTag("floating_whatsapp_fab")
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "Chat on WhatsApp (+27 74 076 7068)"
        )
    }
}

@Composable
fun ProductOrBannerImage(
    drawableKey: String,
    customUri: String = "",
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (customUri.isNotBlank()) {
        AsyncImage(
            model = customUri,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(id = drawableForKey(drawableKey)),
            placeholder = painterResource(id = drawableForKey(drawableKey))
        )
    } else {
        Image(
            painter = painterResource(id = drawableForKey(drawableKey)),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

fun drawableForKey(key: String): Int = when (key) {
    "hero" -> R.drawable.img_hero_logistics
    "freight" -> R.drawable.img_freight_operations
    "clothing" -> R.drawable.img_islamic_clothing
    "accessories" -> R.drawable.img_islamic_accessories
    else -> R.drawable.img_hero_logistics
}

@Composable
fun SectionHeader(
    eyebrow: String,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = eyebrow.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = GoldRich,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GoldOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.5.dp, GoldRich),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldRich),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CorporateFooterCard(
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onOpenWhatsApp: () -> Unit,
    onOpenMaps: () -> Unit
) {
    Surface(
        color = EmeraldDeep,
        contentColor = PureWhite,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IslamicGeometricBackground(
                modifier = Modifier.matchParentSize(),
                lineColor = GoldRich.copy(alpha = 0.08f)
            )
            ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, isTablet ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (isDesktop || isTablet) {
                        // Wide Multi-Column Corporate Footer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(28.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Column 1: Brand & Corporate Overview
                            Column(
                                modifier = Modifier.weight(1.3f),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                FooterBrandBlock(settings)
                            }
                            // Column 2: Official Contact Details
                            Column(
                                modifier = Modifier.weight(1.2f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                FooterContactBlock(settings, onOpenMaps, onOpenWhatsApp)
                            }
                            // Column 3: Logistics Services
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("LOGISTICS SERVICES", style = MaterialTheme.typography.labelMedium, color = GoldBright)
                                FooterTextLink("All Logistics Services") { onNavigate(AppRoutes.ROUTE_LOGISTICS) }
                                FooterTextLink("International Air Freight") { onNavigate(AppRoutes.ROUTE_AIR_FREIGHT) }
                                FooterTextLink("International Sea Freight") { onNavigate(AppRoutes.ROUTE_SEA_FREIGHT) }
                                FooterTextLink("SA Customs Clearance") { onNavigate(AppRoutes.ROUTE_CUSTOMS_CLEARANCE) }
                                FooterTextLink("Global Door-to-Door") { onNavigate(AppRoutes.ROUTE_DOOR_TO_DOOR) }
                                FooterTextLink("SADC Road Freight") { onNavigate(AppRoutes.ROUTE_SADC_ROAD_FREIGHT) }
                            }
                            // Column 4: Islamic Supplies & Legal
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("SUPPLIES & COMPANY", style = MaterialTheme.typography.labelMedium, color = GoldBright)
                                FooterTextLink("Islamic Supplies") { onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) }
                                FooterTextLink("Men's Clothing") { onNavigate(AppRoutes.ROUTE_MENS_CLOTHING) }
                                FooterTextLink("Women's Clothing") { onNavigate(AppRoutes.ROUTE_WOMENS_CLOTHING) }
                                FooterTextLink("Accessories & Books") { onNavigate(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES) }
                                FooterTextLink("Islamic Guidance") { onNavigate(AppRoutes.ROUTE_ISLAMIC_GUIDANCE) }
                                FooterTextLink("About Us") { onNavigate(AppRoutes.ROUTE_ABOUT) }
                                FooterTextLink("Request a Quote") { onNavigate(AppRoutes.ROUTE_QUOTE) }
                                FooterTextLink("Contact Us") { onNavigate(AppRoutes.ROUTE_CONTACT) }
                                FooterTextLink("Privacy Policy") { onNavigate(AppRoutes.ROUTE_PRIVACY_POLICY) }
                                FooterTextLink("Terms & Conditions") { onNavigate(AppRoutes.ROUTE_TERMS) }
                            }
                        }
                    } else {
                        // Mobile Stacked Footer
                        FooterBrandBlock(settings)
                        HorizontalDivider(color = GoldRich.copy(alpha = 0.35f))
                        FooterContactBlock(settings, onOpenMaps, onOpenWhatsApp)
                        Text(
                            text = "QUICK LINKS & SERVICES",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldBright
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FooterLinkChip("About Us") { onNavigate(AppRoutes.ROUTE_ABOUT) }
                            FooterLinkChip("Air Freight") { onNavigate(AppRoutes.ROUTE_AIR_FREIGHT) }
                            FooterLinkChip("Sea Freight") { onNavigate(AppRoutes.ROUTE_SEA_FREIGHT) }
                            FooterLinkChip("SA Customs") { onNavigate(AppRoutes.ROUTE_CUSTOMS_CLEARANCE) }
                            FooterLinkChip("Door-to-Door") { onNavigate(AppRoutes.ROUTE_DOOR_TO_DOOR) }
                            FooterLinkChip("SADC Road") { onNavigate(AppRoutes.ROUTE_SADC_ROAD_FREIGHT) }
                            FooterLinkChip("Men's Clothing") { onNavigate(AppRoutes.ROUTE_MENS_CLOTHING) }
                            FooterLinkChip("Women's Clothing") { onNavigate(AppRoutes.ROUTE_WOMENS_CLOTHING) }
                            FooterLinkChip("Accessories & Books") { onNavigate(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES) }
                            FooterLinkChip("Guidance") { onNavigate(AppRoutes.ROUTE_ISLAMIC_GUIDANCE) }
                            FooterLinkChip("Request Quote") { onNavigate(AppRoutes.ROUTE_QUOTE) }
                            FooterLinkChip("Contact Us") { onNavigate(AppRoutes.ROUTE_CONTACT) }
                            FooterLinkChip("Privacy Policy") { onNavigate(AppRoutes.ROUTE_PRIVACY_POLICY) }
                            FooterLinkChip("Terms") { onNavigate(AppRoutes.ROUTE_TERMS) }
                        }
                    }

                    // Social Channels Status
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "SOCIAL CHANNELS",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldBright
                        )
                        val configuredSocials = listOfNotNull(
                            settings.instagramPlaceholder.takeIf { it.isNotBlank() }?.let { "Instagram: $it" },
                            settings.facebookPlaceholder.takeIf { it.isNotBlank() }?.let { "Facebook: $it" },
                            settings.linkedinPlaceholder.takeIf { it.isNotBlank() }?.let { "LinkedIn: $it" }
                        )
                        if (configuredSocials.isEmpty()) {
                            Text(
                                text = "Social media profile links: Configurable in Admin Settings (none published yet).",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureWhite.copy(alpha = 0.72f)
                            )
                        } else {
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                configuredSocials.forEach { label ->
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PureWhite.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = GoldRich.copy(alpha = 0.25f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "© ${settings.businessName} — Springs, Gauteng, South Africa",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite.copy(alpha = 0.78f)
                        )
                        Surface(
                            color = GoldRich.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigate(AppRoutes.ROUTE_ADMIN) }
                                .testTag("footer_admin_link")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AdminPanelSettings,
                                    contentDescription = "Admin Portal",
                                    tint = GoldBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Admin Portal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldBright
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FooterBrandBlock(settings: BusinessSettingsEntity) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_art),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = settings.businessName,
                style = MaterialTheme.typography.titleMedium,
                color = GoldBright
            )
            Text(
                text = "International Freight Forwarding & Islamic Supplies",
                style = MaterialTheme.typography.bodySmall,
                color = PureWhite.copy(alpha = 0.85f)
            )
        }
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "Connecting businesses, individuals, and communities through dependable cargo transportation, South African customs clearance coordination, and quality Islamic supplies.",
        style = MaterialTheme.typography.bodySmall,
        color = PureWhite.copy(alpha = 0.82f)
    )
}

@Composable
private fun FooterContactBlock(
    settings: BusinessSettingsEntity,
    onOpenMaps: () -> Unit,
    onOpenWhatsApp: () -> Unit
) {
    Text(
        text = "OFFICIAL CONTACT DETAILS",
        style = MaterialTheme.typography.labelMedium,
        color = GoldBright
    )
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onOpenMaps)
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Business Address",
            tint = GoldBright,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = settings.address,
            style = MaterialTheme.typography.bodySmall,
            color = PureWhite
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onOpenWhatsApp)
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "WhatsApp Number",
            tint = WhatsAppGreen,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "WhatsApp: ${settings.whatsappNumber} (Tap to Chat)",
            style = MaterialTheme.typography.bodySmall,
            color = PureWhite
        )
    }
    if (settings.emailPlaceholder.isNotBlank()) {
        Text(
            text = "Email: ${settings.emailPlaceholder}",
            style = MaterialTheme.typography.bodySmall,
            color = PureWhite.copy(alpha = 0.85f)
        )
    }
    if (settings.phonePlaceholder.isNotBlank()) {
        Text(
            text = "Telephone: ${settings.phonePlaceholder}",
            style = MaterialTheme.typography.bodySmall,
            color = PureWhite.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun FooterTextLink(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = PureWhite.copy(alpha = 0.88f),
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun FooterLinkChip(label: String, onClick: () -> Unit) {
    Surface(
        color = PureWhite.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = PureWhite,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
