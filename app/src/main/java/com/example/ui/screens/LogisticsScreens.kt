package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.BusinessSettingsEntity
import com.example.data.model.LogisticsServiceType
import com.example.data.model.SadcCountryConfig
import com.example.ui.components.CorporateFooterCard
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.ResponsiveGrid
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldMedium
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppMessages

data class LogisticsServiceContent(
    val type: LogisticsServiceType,
    val route: String,
    val title: String,
    val tagline: String,
    val description: String,
    val imageRes: Int,
    val icon: ImageVector,
    val scopeItems: List<String>,
    val benefits: List<String>,
    val howItWorks: List<String>,
    val whatsAppMessage: String,
    val complianceNote: String? = null
)

val LogisticsServicesCatalog = listOf(
    LogisticsServiceContent(
        type = LogisticsServiceType.AIR_FREIGHT,
        route = AppRoutes.ROUTE_AIR_FREIGHT,
        title = "International Air Freight",
        tagline = "Time-Sensitive Global Air Cargo Coordination",
        description = "We coordinate international air freight solutions for cargo moving between global origins and destinations, supporting commercial consignments, urgent shipments, and structured airport-to-airport or door-to-door air cargo workflows.",
        imageRes = R.drawable.img_hero_logistics,
        icon = Icons.Default.FlightTakeoff,
        scopeItems = listOf(
            "International air cargo coordination",
            "Import and export coordination",
            "Airport cargo handling arrangements",
            "Documentation assistance",
            "Shipment coordination and status updates",
            "Door-to-door air freight arrangements"
        ),
        benefits = listOf(
            "Priority transit coordination for urgent or high-value commercial freight",
            "Clear pre-shipment weight, dimensions, and airway bill readiness",
            "Seamless alignment with South African customs clearance on arrival"
        ),
        howItWorks = listOf(
            "Submit origin airport/city, destination, cargo dimensions, and weight.",
            "Receive an air freight coordination assessment and required document checklist.",
            "Confirm collection and airway booking arrangements.",
            "Cargo moves through airport handling and customs release to final delivery."
        ),
        whatsAppMessage = WhatsAppMessages.AIR_FREIGHT
    ),
    LogisticsServiceContent(
        type = LogisticsServiceType.SEA_FREIGHT,
        route = AppRoutes.ROUTE_SEA_FREIGHT,
        title = "International Sea Freight",
        tagline = "FCL & LCL Ocean Container Freight Forwarding",
        description = "Reliable international sea freight coordination linking South African maritime ports with global trade lanes. Whether shipping full containers or consolidated part-loads, we coordinate every stage of ocean transport.",
        imageRes = R.drawable.img_hero_logistics,
        icon = Icons.Default.DirectionsBoat,
        scopeItems = listOf(
            "Full Container Load (FCL) coordination",
            "Less than Container Load (LCL) consolidation",
            "International sea freight coordination",
            "Port handling arrangements",
            "Shipping documentation assistance (Bill of Lading, Packing List, Commercial Invoice)",
            "Customs clearance coordination",
            "Port-to-door cargo delivery arrangements"
        ),
        benefits = listOf(
            "Cost-effective ocean capacity for bulk commercial cargo and wholesale Islamic supplies",
            "Flexible FCL (20ft/40ft) or LCL shared-container arrangements",
            "Coordinated port release and onward inland road delivery"
        ),
        howItWorks = listOf(
            "Indicate whether your shipment requires FCL, LCL, or professional advice.",
            "We review port-pair routing, container requirements, and shipping paperwork.",
            "Confirm vessel booking and collection/stuffing arrangements.",
            "We coordinate arrival port handling, customs clearance, and final delivery."
        ),
        whatsAppMessage = WhatsAppMessages.SEA_FREIGHT
    ),
    LogisticsServiceContent(
        type = LogisticsServiceType.CUSTOMS_CLEARANCE,
        route = AppRoutes.ROUTE_CUSTOMS_CLEARANCE,
        title = "South African Customs Clearance",
        tagline = "Import & Export Customs Coordination in South Africa",
        description = "South African customs clearance is one of our core operational focus areas. We assist importers and exporters with coordinating customs clearance processes for goods entering and leaving South Africa.",
        imageRes = R.drawable.img_freight_operations,
        icon = Icons.Default.Gavel,
        scopeItems = listOf(
            "Import customs clearance coordination",
            "Export documentation support",
            "Customs declarations and documentation assistance",
            "Customs duty and tax information coordination",
            "Cargo release arrangements",
            "Import and export logistics support"
        ),
        benefits = listOf(
            "Structured document verification to reduce avoidable port or airport delays",
            "Clear guidance on commercial invoices, packing lists, and supporting paperwork",
            "Unified coordination between customs release and onward local delivery"
        ),
        howItWorks = listOf(
            "Share your commercial invoice, packing list, transport document, and goods classification.",
            "We review documentation readiness and coordinate declaration requirements.",
            "Duties, VAT/taxes, and regulatory checks are assessed according to official tariff rules.",
            "Upon official customs release, cargo delivery or collection is arranged."
        ),
        whatsAppMessage = WhatsAppMessages.CUSTOMS_CLEARANCE,
        complianceNote = "Regulatory Notice: All customs clearance activities are strictly subject to applicable South African laws, SARS customs requirements, supporting documentation, tariff classifications, and official regulatory approval. AL-HALAL LOGISTICS N TRANS does not claim specific government licences, registrations, or official authorisations beyond verified business disclosures."
    ),
    LogisticsServiceContent(
        type = LogisticsServiceType.DOOR_TO_DOOR,
        route = AppRoutes.ROUTE_DOOR_TO_DOOR,
        title = "Global Door-to-Door Delivery",
        tagline = "Coordinated End-to-End Cargo Collection & Delivery",
        description = "We coordinate international cargo movements from supplier collection all the way to the recipient's final destination, combining air or sea freight with documentation support and last-mile delivery.",
        imageRes = R.drawable.img_hero_logistics,
        icon = Icons.Default.Public,
        scopeItems = listOf(
            "International collection arrangements",
            "Air and sea freight mode options",
            "Cargo handling coordination across transit hubs",
            "Destination customs & delivery arrangements",
            "Consistent communication throughout the shipping process"
        ),
        benefits = listOf(
            "Single coordinated point of contact from origin pickup to destination doorstep",
            "Eliminates handoff gaps between freight legs and local delivery",
            "Transparent updates from collection to final proof of delivery"
        ),
        howItWorks = listOf(
            "Provide collection address, delivery address, cargo details, and timeline needs.",
            "We propose the suitable air or sea door-to-door logistics plan.",
            "Confirm pickup schedule and export/import documentation.",
            "Track milestone progress until final cargo handover."
        ),
        whatsAppMessage = WhatsAppMessages.DOOR_TO_DOOR
    ),
    LogisticsServiceContent(
        type = LogisticsServiceType.SADC_ROAD_FREIGHT,
        route = AppRoutes.ROUTE_SADC_ROAD_FREIGHT,
        title = "SADC Road Freight",
        tagline = "Regional Cross-Border Cargo Across Southern Africa",
        description = "Dedicated commercial and general road cargo transportation connecting South Africa with regional markets across the Southern African Development Community (SADC).",
        imageRes = R.drawable.img_freight_operations,
        icon = Icons.Default.LocalShipping,
        scopeItems = listOf(
            "Cross-border cargo transportation",
            "Commercial freight coordination",
            "General cargo movements",
            "Regional collection and delivery",
            "Cross-border documentation coordination",
            "Dedicated road freight enquiries"
        ),
        benefits = listOf(
            "Coordinated regional road links from Gauteng & South African hubs into SADC corridors",
            "Supports palletized, boxed, commercial, and project cargo requirements",
            "Border documentation preparedness before dispatch"
        ),
        howItWorks = listOf(
            "Specify collection point, destination SADC country/city, and vehicle or cargo specs.",
            "We assess corridor availability and border documentation requirements.",
            "Confirm vehicle allocation and loading schedule.",
            "Coordinated cross-border transit and regional delivery."
        ),
        whatsAppMessage = WhatsAppMessages.ROAD_FREIGHT
    )
)

@Composable
fun LogisticsOverviewScreen(
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
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_freight_operations),
                    contentDescription = "AL-HALAL Global Logistics Operations",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(EmeraldDeep.copy(alpha = 0.82f))
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ResponsiveContainer { isDesktop, _ ->
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "DIVISION ONE",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldBright
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "AL-HALAL GLOBAL LOGISTICS",
                                style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "International freight forwarding, South African customs clearance, global door-to-door delivery, and SADC road cargo.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = PureWhite.copy(alpha = 0.92f),
                                modifier = Modifier.widthIn(max = 760.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SectionHeader(
                        eyebrow = "Core Freight Solutions",
                        title = "Explore Every Logistics Service"
                    )
                    ResponsiveGrid(
                        items = LogisticsServicesCatalog,
                        columns = when {
                            isDesktop -> 3
                            isTablet -> 2
                            else -> 1
                        },
                        horizontalGap = 18.dp,
                        verticalGap = 18.dp
                    ) { service ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Image(
                                    painter = painterResource(id = service.imageRes),
                                    contentDescription = service.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(if (isDesktop) 170.dp else 145.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(service.icon, contentDescription = null, tint = GoldRich)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(service.title, style = MaterialTheme.typography.headlineMedium)
                                    }
                                    Text(
                                        text = service.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { onNavigate(service.route) },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                        ) {
                                            Text("View Service", style = MaterialTheme.typography.labelMedium)
                                        }
                                        Button(
                                            onClick = { onRequestQuote(service.type) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = GoldRich,
                                                contentColor = CharcoalInk
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                        ) {
                                            Text("Enquire", style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            CorporateFooterCard(
                settings = settings,
                onNavigate = onNavigate,
                onOpenWhatsApp = { onOpenWhatsApp(WhatsAppMessages.GENERAL_ENQUIRY) },
                onOpenMaps = onOpenMaps
            )
        }
    }
}

@Composable
fun LogisticsServiceDetailScreen(
    service: LogisticsServiceContent,
    sadcCountries: List<SadcCountryConfig>,
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onRequestQuote: (LogisticsServiceType) -> Unit,
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
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = service.imageRes),
                    contentDescription = service.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(EmeraldDeep.copy(alpha = 0.68f), EmeraldDeep.copy(alpha = 0.94f))
                            )
                        )
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    ResponsiveContainer(verticalPadding = 24.dp) { isDesktop, _ ->
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(service.icon, contentDescription = null, tint = GoldBright, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(service.tagline, style = MaterialTheme.typography.labelMedium, color = GoldBright)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = service.title,
                                style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                                color = PureWhite
                            )
                        }
                    }
                }
            }
        }

        item {
            ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.widthIn(max = 920.dp)
                    )

                    if (!service.complianceNote.isNullOrBlank()) {
                        Surface(
                            color = StatusWarningBg,
                            border = BorderStroke(1.dp, StatusWarning.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Regulatory Notice",
                                    tint = StatusWarning,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = service.complianceNote,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalInk
                                )
                            }
                        }
                    }

                    // 2-Column Scope + Benefits on Tablet/Desktop
                    if (isDesktop || isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            ServiceBulletCard(
                                title = "What We Coordinate",
                                items = service.scopeItems,
                                modifier = Modifier.weight(1f)
                            )
                            ServiceBulletCard(
                                title = "Key Benefits",
                                items = service.benefits,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        ServiceBulletCard(title = "What We Coordinate", items = service.scopeItems)
                        ServiceBulletCard(title = "Key Benefits", items = service.benefits)
                    }

                    // How It Works
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text("How This Service Works", style = MaterialTheme.typography.headlineMedium)
                            val numberedSteps = service.howItWorks.mapIndexed { index, s -> (index + 1) to s }
                            ResponsiveGrid(
                                items = numberedSteps,
                                columns = if (isDesktop) 4 else if (isTablet) 2 else 1,
                                horizontalGap = 14.dp,
                                verticalGap = 12.dp
                            ) { (num, step) ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(GoldRich)
                                    ) {
                                        Text(
                                            text = "$num",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CharcoalInk,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(step, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    if (service.type == LogisticsServiceType.SADC_ROAD_FREIGHT) {
                        SadcRegionalMapSection(
                            sadcCountries = sadcCountries,
                            isWide = isDesktop || isTablet
                        )
                    }

                    Row(
                        modifier = Modifier.widthIn(max = if (isDesktop || isTablet) 560.dp else 1000.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onRequestQuote(service.type) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldRich,
                                contentColor = CharcoalInk
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("service_quote_button")
                        ) {
                            Icon(Icons.Default.RequestQuote, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Request Quote", style = MaterialTheme.typography.labelLarge)
                        }
                        Button(
                            onClick = { onOpenWhatsApp(service.whatsAppMessage) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WhatsAppGreen,
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("service_whatsapp_button")
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }

        item {
            CorporateFooterCard(
                settings = settings,
                onNavigate = onNavigate,
                onOpenWhatsApp = { onOpenWhatsApp(service.whatsAppMessage) },
                onOpenMaps = onOpenMaps
            )
        }
    }
}

@Composable
private fun ServiceBulletCard(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
            items.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldMedium,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(item, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SadcRegionalMapSection(
    sadcCountries: List<SadcCountryConfig>,
    isWide: Boolean = false
) {
    var selectedCountry by remember(sadcCountries) {
        mutableStateOf(sadcCountries.firstOrNull())
    }

    val pinPositions = remember {
        mapOf(
            "ZA" to Offset(0.47f, 0.80f),
            "LS" to Offset(0.55f, 0.81f),
            "SZ" to Offset(0.62f, 0.74f),
            "BW" to Offset(0.45f, 0.62f),
            "NA" to Offset(0.24f, 0.59f),
            "ZW" to Offset(0.57f, 0.53f),
            "MZ" to Offset(0.73f, 0.52f),
            "ZM" to Offset(0.52f, 0.37f),
            "MW" to Offset(0.67f, 0.35f),
            "CD" to Offset(0.44f, 0.16f),
            "TZ" to Offset(0.69f, 0.18f)
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, GoldRich.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                eyebrow = "Regional Corridor Coverage",
                title = "Southern Africa (SADC) Freight Map",
                subtitle = "Service availability by country is configurable in Admin Settings. Corridor coordination originates from South Africa and does not imply physical branch offices in every SADC state."
            )

            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    SadcMapCanvasBox(
                        sadcCountries = sadcCountries,
                        selectedCountry = selectedCountry,
                        pinPositions = pinPositions,
                        modifier = Modifier
                            .weight(1.1f)
                            .height(310.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SadcCountryChipsAndDetail(
                            sadcCountries = sadcCountries,
                            selectedCountry = selectedCountry,
                            onSelect = { selectedCountry = it }
                        )
                    }
                }
            } else {
                SadcMapCanvasBox(
                    sadcCountries = sadcCountries,
                    selectedCountry = selectedCountry,
                    pinPositions = pinPositions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                SadcCountryChipsAndDetail(
                    sadcCountries = sadcCountries,
                    selectedCountry = selectedCountry,
                    onSelect = { selectedCountry = it }
                )
            }
        }
    }
}

@Composable
private fun SadcMapCanvasBox(
    sadcCountries: List<SadcCountryConfig>,
    selectedCountry: SadcCountryConfig?,
    pinPositions: Map<String, Offset>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(EmeraldDeep)
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val africaPath = Path().apply {
                moveTo(w * 0.18f, h * 0.10f)
                lineTo(w * 0.78f, h * 0.10f)
                lineTo(w * 0.83f, h * 0.36f)
                lineTo(w * 0.76f, h * 0.60f)
                lineTo(w * 0.67f, h * 0.78f)
                lineTo(w * 0.54f, h * 0.92f)
                lineTo(w * 0.35f, h * 0.90f)
                lineTo(w * 0.25f, h * 0.72f)
                lineTo(w * 0.16f, h * 0.45f)
                close()
            }
            drawPath(path = africaPath, color = EmeraldSoft.copy(alpha = 0.16f))
            drawPath(
                path = africaPath,
                color = GoldBright.copy(alpha = 0.55f),
                style = Stroke(width = 1.8.dp.toPx())
            )

            val hub = pinPositions["ZA"] ?: Offset(0.47f, 0.80f)
            val hubPx = Offset(hub.x * w, hub.y * h)

            sadcCountries.forEach { country ->
                val p = pinPositions[country.code] ?: return@forEach
                val pt = Offset(p.x * w, p.y * h)
                if (country.code != "ZA" && country.isServiceEnabled) {
                    drawLine(
                        color = GoldBright.copy(alpha = 0.75f),
                        start = hubPx,
                        end = pt,
                        strokeWidth = 1.6.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    )
                }
                val isSelected = selectedCountry?.code == country.code
                val pinColor = when {
                    country.code == "ZA" -> GoldBright
                    country.isServiceEnabled -> WhatsAppGreen
                    else -> Color.LightGray.copy(alpha = 0.6f)
                }
                if (isSelected) {
                    drawCircle(
                        color = PureWhite.copy(alpha = 0.35f),
                        radius = 14.dp.toPx(),
                        center = pt
                    )
                }
                drawCircle(
                    color = pinColor,
                    radius = if (country.code == "ZA") 8.dp.toPx() else 6.dp.toPx(),
                    center = pt
                )
            }
        }
        Text(
            text = "Hub: Springs, Gauteng (ZA)",
            style = MaterialTheme.typography.labelSmall,
            color = GoldBright,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SadcCountryChipsAndDetail(
    sadcCountries: List<SadcCountryConfig>,
    selectedCountry: SadcCountryConfig?,
    onSelect: (SadcCountryConfig) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sadcCountries.forEach { country ->
            FilterChip(
                selected = selectedCountry?.code == country.code,
                onClick = { onSelect(country) },
                label = {
                    Text(
                        text = "${country.name}${if (country.isServiceEnabled) "" else " (Enquiry)"}",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = EmeraldDeep,
                    selectedLabelColor = GoldBright
                )
            )
        }
    }

    selectedCountry?.let { c ->
        Surface(
            color = EmeraldSoft,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "${c.name} — ${if (c.isServiceEnabled) "Active Road Corridor" else "On-Request / Partner Assessment"}",
                    style = MaterialTheme.typography.titleSmall,
                    color = EmeraldDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = c.corridorNote,
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalInk
                )
            }
        }
    }
}
