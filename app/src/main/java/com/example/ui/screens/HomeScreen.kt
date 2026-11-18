package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.BusinessSettingsEntity
import com.example.data.model.LogisticsServiceType
import com.example.ui.components.CorporateFooterCard
import com.example.ui.components.IslamicGeometricBackground
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.ResponsiveGrid
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.GoldSoft
import com.example.ui.theme.PureWhite
import com.example.ui.theme.WhatsAppGreen

private data class HomeServiceItem(
    val icon: ImageVector,
    val title: String,
    val summary: String,
    val route: String
)

private data class ShowcaseTileItem(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
    val route: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onRequestQuote: (LogisticsServiceType?) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 84.dp)
    ) {
        // 1. FULL-WIDTH HERO SECTION
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 380.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_logistics),
                    contentDescription = "International cargo ship and freight aircraft at golden hour",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    EmeraldDeep.copy(alpha = 0.92f),
                                    CharcoalInk.copy(alpha = 0.80f),
                                    EmeraldDeep.copy(alpha = 0.68f)
                                )
                            )
                        )
                )
                IslamicGeometricBackground(
                    modifier = Modifier.matchParentSize(),
                    lineColor = GoldRich.copy(alpha = 0.08f)
                )
                ResponsiveContainer(verticalPadding = 36.dp) { isDesktop, isTablet ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (isDesktop) 44.dp else if (isTablet) 24.dp else 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = GoldRich.copy(alpha = 0.22f),
                            border = BorderStroke(1.dp, GoldBright),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "SOUTH AFRICA • SADC • GLOBAL FREIGHT & SUPPLIES",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldBright,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = settings.heroHeadline,
                            style = if (isDesktop) {
                                MaterialTheme.typography.displayMedium.copy(
                                    fontSize = 46.sp,
                                    lineHeight = 54.sp
                                )
                            } else if (isTablet) {
                                MaterialTheme.typography.displaySmall.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 44.sp
                                )
                            } else {
                                MaterialTheme.typography.displaySmall
                            },
                            color = PureWhite,
                            modifier = Modifier.widthIn(max = 880.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = settings.heroSubheading,
                            style = if (isDesktop) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                            color = PureWhite.copy(alpha = 0.92f),
                            modifier = Modifier.widthIn(max = 720.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { onNavigate(AppRoutes.ROUTE_LOGISTICS) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = PureWhite
                                ),
                                border = BorderStroke(1.dp, GoldBright.copy(alpha = 0.65f)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("hero_explore_logistics_btn")
                            ) {
                                Text("EXPLORE LOGISTICS SERVICES", style = MaterialTheme.typography.labelMedium)
                            }
                            OutlinedButton(
                                onClick = { onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) },
                                border = BorderStroke(1.5.dp, PureWhite),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("hero_discover_islamic_btn")
                            ) {
                                Text("EXPLORE ISLAMIC SUPPLIES", style = MaterialTheme.typography.labelMedium)
                            }
                            Button(
                                onClick = { onRequestQuote(null) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldRich,
                                    contentColor = CharcoalInk
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("hero_request_quote_btn")
                            ) {
                                Icon(Icons.Default.RequestQuote, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("REQUEST A QUOTE", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // A. OUR BUSINESS DIVISIONS (Side-by-Side Desktop Panels)
        item {
            ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SectionHeader(
                        eyebrow = "Unified Corporate Identity",
                        title = "Our Business Divisions",
                        subtitle = "Two specialized divisions operating with integrity, reliability, and international connectivity."
                    )
                    if (isDesktop || isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            DivisionCard(
                                title = "AL-HALAL GLOBAL LOGISTICS",
                                description = "International air freight, sea freight, South African customs clearance, global door-to-door delivery and SADC regional road transportation.",
                                buttonText = "EXPLORE LOGISTICS",
                                imageRes = R.drawable.img_freight_operations,
                                imageDesc = "Freight operations and cross-border cargo trucks",
                                accentColor = EmeraldDeep,
                                imageHeight = if (isDesktop) 240 else 200,
                                testTag = "division_logistics_btn",
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate(AppRoutes.ROUTE_LOGISTICS) }
                            )
                            DivisionCard(
                                title = "AL-HALAL ISLAMIC SUPPLIES",
                                description = "Supply, sourcing and worldwide distribution of Islamic clothing, modest fashion, prayer mats, tasbeeh and Islamic lifestyle products.",
                                buttonText = "EXPLORE ISLAMIC SUPPLIES",
                                imageRes = R.drawable.img_islamic_clothing,
                                imageDesc = "Modest Islamic clothing including thobe, abaya, and hijab",
                                accentColor = GoldRich,
                                imageHeight = if (isDesktop) 240 else 200,
                                testTag = "division_islamic_btn",
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) }
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            DivisionCard(
                                title = "AL-HALAL GLOBAL LOGISTICS",
                                description = "International air freight, sea freight, customs clearance, door-to-door delivery and SADC road transportation.",
                                buttonText = "EXPLORE LOGISTICS",
                                imageRes = R.drawable.img_freight_operations,
                                imageDesc = "Freight operations and cross-border cargo trucks",
                                accentColor = EmeraldDeep,
                                imageHeight = 170,
                                testTag = "division_logistics_btn",
                                onClick = { onNavigate(AppRoutes.ROUTE_LOGISTICS) }
                            )
                            DivisionCard(
                                title = "AL-HALAL ISLAMIC SUPPLIES",
                                description = "Islamic clothing, modest fashion, prayer accessories and general Islamic lifestyle products.",
                                buttonText = "EXPLORE ISLAMIC SUPPLIES",
                                imageRes = R.drawable.img_islamic_clothing,
                                imageDesc = "Modest Islamic clothing including thobe, abaya, and hijab",
                                accentColor = GoldRich,
                                imageHeight = 170,
                                testTag = "division_islamic_btn",
                                onClick = { onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) }
                            )
                        }
                    }
                }
            }
        }

        // B. OUR LOGISTICS SERVICES (Full-Width Subtle Band + Multi-Column Grid)
        item {
            val services = listOf(
                HomeServiceItem(
                    icon = Icons.Default.FlightTakeoff,
                    title = "International Air Freight",
                    summary = "Time-critical international air cargo coordination, airport handling, and export/import solutions.",
                    route = AppRoutes.ROUTE_AIR_FREIGHT
                ),
                HomeServiceItem(
                    icon = Icons.Default.DirectionsBoat,
                    title = "International Sea Freight",
                    summary = "Full Container Load (FCL) and Less than Container Load (LCL) ocean shipping with port coordination.",
                    route = AppRoutes.ROUTE_SEA_FREIGHT
                ),
                HomeServiceItem(
                    icon = Icons.Default.Gavel,
                    title = "SA Customs Clearance",
                    summary = "South African import & export customs documentation support, declarations, and release coordination.",
                    route = AppRoutes.ROUTE_CUSTOMS_CLEARANCE
                ),
                HomeServiceItem(
                    icon = Icons.Default.Public,
                    title = "Door-to-Door Delivery",
                    summary = "End-to-end collection, freight transit, customs coordination, and final destination delivery.",
                    route = AppRoutes.ROUTE_DOOR_TO_DOOR
                ),
                HomeServiceItem(
                    icon = Icons.Default.LocalShipping,
                    title = "SADC Road Freight",
                    summary = "Regional cross-border commercial road transportation connecting South Africa and SADC corridors.",
                    route = AppRoutes.ROUTE_SADC_ROAD_FREIGHT
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            ) {
                ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, isTablet ->
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        SectionHeader(
                            eyebrow = "Division One",
                            title = "Our Logistics Services",
                            subtitle = "Comprehensive freight forwarding and South African import/export coordination."
                        )
                        ResponsiveGrid(
                            items = services,
                            columns = when {
                                isDesktop -> 3
                                isTablet -> 2
                                else -> 1
                            },
                            horizontalGap = 16.dp,
                            verticalGap = 14.dp
                        ) { svc ->
                            HomeServiceRowCard(
                                icon = svc.icon,
                                title = svc.title,
                                summary = svc.summary,
                                onClick = { onNavigate(svc.route) }
                            )
                        }
                    }
                }
            }
        }

        // C. WHY CHOOSE US? (Multi-Column Corporate Pillars)
        item {
            val pillars = listOf(
                "International logistics coordination",
                "Multiple transportation solutions",
                "South African import clearance support",
                "Regional road transportation",
                "Customer-focused service",
                "Islamic values and business integrity"
            )
            ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    SectionHeader(
                        eyebrow = "Our Commitment",
                        title = "Why Choose Us?"
                    )
                    ResponsiveGrid(
                        items = pillars,
                        columns = when {
                            isDesktop -> 3
                            isTablet -> 2
                            else -> 1
                        },
                        horizontalGap = 14.dp,
                        verticalGap = 12.dp
                    ) { pillar ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = GoldRich,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = pillar,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // D. ISLAMIC SUPPLIES SHOWCASE (4-Column Desktop Image Grid)
        item {
            val tiles = listOf(
                ShowcaseTileItem("Men's Clothing", "Thobes, Jubbas & Kufi Caps", R.drawable.img_islamic_clothing, AppRoutes.ROUTE_MENS_CLOTHING),
                ShowcaseTileItem("Women's Modest Wear", "Abayas, Hijabs & Khimars", R.drawable.img_islamic_clothing, AppRoutes.ROUTE_WOMENS_CLOTHING),
                ShowcaseTileItem("Prayer Accessories", "Prayer Mats & Tasbeeh", R.drawable.img_islamic_accessories, AppRoutes.ROUTE_ISLAMIC_ACCESSORIES),
                ShowcaseTileItem("Islamic Guidance", "Product & Modest Wear Guide", R.drawable.img_islamic_accessories, AppRoutes.ROUTE_ISLAMIC_GUIDANCE)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldSoft.copy(alpha = 0.55f))
            ) {
                IslamicGeometricBackground(
                    modifier = Modifier.matchParentSize(),
                    lineColor = GoldRich.copy(alpha = 0.12f)
                )
                ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, _ ->
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        SectionHeader(
                            eyebrow = "Division Two",
                            title = "Islamic Supplies Showcase",
                            subtitle = "Thoughtfully sourced modest garments, prayer essentials, and educational materials."
                        )
                        ResponsiveGrid(
                            items = tiles,
                            columns = if (isDesktop) 4 else 2,
                            horizontalGap = 16.dp,
                            verticalGap = 16.dp
                        ) { tile ->
                            ShowcaseMiniCard(
                                title = tile.title,
                                subtitle = tile.subtitle,
                                imageRes = tile.imageRes,
                                imageHeight = if (isDesktop) 150 else 115,
                                onClick = { onNavigate(tile.route) }
                            )
                        }
                    }
                }
            }
        }

        // E. HOW OUR LOGISTICS PROCESS WORKS (4 Horizontal Steps on Desktop)
        item {
            val steps = listOf(
                "1" to ("Send Your Enquiry" to "Submit your cargo details via our Quotation Form or direct WhatsApp line."),
                "2" to ("Receive Your Assessment" to "Our team reviews routing, mode options (Air, Sea, or SADC Road), and documentation."),
                "3" to ("Confirm Arrangements" to "Approve the coordinated schedule, customs requirements, and collection plan."),
                "4" to ("Transport & Delivery" to "Coordinated cargo movement with clear communication through to destination delivery.")
            )
            ResponsiveContainer(verticalPadding = 32.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SectionHeader(
                        eyebrow = "Transparent Workflow",
                        title = "How Our Logistics Process Works"
                    )
                    ResponsiveGrid(
                        items = steps,
                        columns = when {
                            isDesktop -> 4
                            isTablet -> 2
                            else -> 1
                        },
                        horizontalGap = 16.dp,
                        verticalGap = 14.dp
                    ) { (num, pair) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldDeep)
                                ) {
                                    Text(
                                        text = num,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = GoldBright,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = pair.first,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = pair.second,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // F. CONTACT AND CONVERSION SECTION
        item {
            ResponsiveContainer(verticalPadding = 12.dp) { isDesktop, isTablet ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldSoft),
                    border = BorderStroke(1.5.dp, GoldRich),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isDesktop || isTablet) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1.4f)
                                    .padding(end = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "READY TO SHIP OR SOURCE ISLAMIC SUPPLIES?",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GoldRich,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Speak directly with AL-HALAL LOGISTICS N TRANS on WhatsApp or submit a detailed quotation request.",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = CharcoalInk
                                )
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { onOpenWhatsApp(com.example.util.WhatsAppMessages.GENERAL_ENQUIRY) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WhatsAppGreen,
                                        contentColor = PureWhite
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("home_whatsapp_cta")
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("WhatsApp", style = MaterialTheme.typography.labelLarge)
                                }
                                Button(
                                    onClick = { onRequestQuote(null) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldDeep,
                                        contentColor = PureWhite
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("home_quote_cta")
                                ) {
                                    Text("Get a Quote", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "READY TO SHIP OR SOURCE ISLAMIC SUPPLIES?",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldRich,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Speak directly with AL-HALAL LOGISTICS N TRANS on WhatsApp or submit a detailed quotation request.",
                                style = MaterialTheme.typography.headlineMedium,
                                color = CharcoalInk
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onOpenWhatsApp(com.example.util.WhatsAppMessages.GENERAL_ENQUIRY) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WhatsAppGreen,
                                        contentColor = PureWhite
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("home_whatsapp_cta")
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("WhatsApp", style = MaterialTheme.typography.labelLarge)
                                }
                                Button(
                                    onClick = { onRequestQuote(null) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldDeep,
                                        contentColor = PureWhite
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("home_quote_cta")
                                ) {
                                    Text("Get a Quote", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // FOOTER
        item {
            CorporateFooterCard(
                settings = settings,
                onNavigate = onNavigate,
                onOpenWhatsApp = { onOpenWhatsApp(com.example.util.WhatsAppMessages.GENERAL_ENQUIRY) },
                onOpenMaps = onOpenMaps
            )
        }
    }
}

@Composable
private fun DivisionCard(
    title: String,
    description: String,
    buttonText: String,
    imageRes: Int,
    imageDesc: String,
    accentColor: Color,
    imageHeight: Int,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.55f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = imageDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (accentColor == GoldRich) GoldRich else EmeraldDeep,
                        contentColor = if (accentColor == GoldRich) CharcoalInk else PureWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag(testTag)
                ) {
                    Text(buttonText, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeServiceRowCard(
    icon: ImageVector,
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = EmeraldSoft,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EmeraldDeep,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open $title",
                tint = GoldRich
            )
        }
    }
}

@Composable
private fun ShowcaseMiniCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    imageHeight: Int = 115,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
