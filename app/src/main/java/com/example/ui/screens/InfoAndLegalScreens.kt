package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.BusinessSettingsEntity
import com.example.ui.components.CorporateFooterCard
import com.example.ui.components.IslamicGeometricBackground
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.ResponsiveGrid
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppMessages

@Composable
fun AboutUsScreen(
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
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
                    .background(EmeraldDeep)
            ) {
                IslamicGeometricBackground(
                    modifier = Modifier.matchParentSize(),
                    lineColor = GoldRich.copy(alpha = 0.12f)
                )
                ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, _ ->
                    Column {
                        Text("ABOUT US", style = MaterialTheme.typography.labelMedium, color = GoldBright)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = settings.businessName,
                            style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "International Logistics, Freight Forwarding & Islamic Supplies",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PureWhite.copy(alpha = 0.90f)
                        )
                    }
                }
            }
        }

        item {
            ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Who We Are", style = MaterialTheme.typography.headlineMedium)
                            Text(text = settings.aboutDescription, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    // 2-Column Vision & Mission on Tablet/Desktop
                    if (isDesktop || isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            VisionCard(modifier = Modifier.weight(1f))
                            MissionCard(modifier = Modifier.weight(1f))
                        }
                    } else {
                        VisionCard()
                        MissionCard()
                    }

                    SectionHeader(eyebrow = "Guiding Principles", title = "Our Values")
                    val values = listOf(
                        "Integrity", "Reliability", "Professionalism", "Transparency",
                        "Trust", "International connectivity", "Respect for Islamic values", "Customer satisfaction"
                    )
                    ResponsiveGrid(
                        items = values,
                        columns = when {
                            isDesktop -> 4
                            isTablet -> 2
                            else -> 1
                        },
                        horizontalGap = 14.dp,
                        verticalGap = 12.dp
                    ) { v ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GoldRich, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(v, style = MaterialTheme.typography.titleSmall)
                            }
                        }
                    }

                    SectionHeader(eyebrow = "Corporate Structure", title = "Our Business Divisions")
                    if (isDesktop || isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            DivisionSummaryCard(
                                title = "1. AL-HALAL GLOBAL LOGISTICS",
                                description = "International freight forwarding, South African customs clearance coordination, air freight, sea freight, door-to-door cargo delivery, and SADC regional road transportation.",
                                isGold = false,
                                modifier = Modifier.weight(1f)
                            )
                            DivisionSummaryCard(
                                title = "2. AL-HALAL ISLAMIC SUPPLIES",
                                description = "Supply, sourcing, and distribution of Islamic clothing, modest fashion, prayer accessories, books, and general Islamic lifestyle products worldwide.",
                                isGold = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        DivisionSummaryCard(
                            title = "1. AL-HALAL GLOBAL LOGISTICS",
                            description = "International freight forwarding, South African customs clearance coordination, air freight, sea freight, door-to-door cargo delivery, and SADC regional road transportation.",
                            isGold = false
                        )
                        DivisionSummaryCard(
                            title = "2. AL-HALAL ISLAMIC SUPPLIES",
                            description = "Supply, sourcing, and distribution of Islamic clothing, modest fashion, prayer accessories, books, and general Islamic lifestyle products worldwide.",
                            isGold = true
                        )
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
private fun VisionCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = EmeraldSoft),
        border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Our Vision", style = MaterialTheme.typography.headlineMedium, color = EmeraldDeep)
            Text(
                text = "To be a trusted bridge connecting Southern Africa and global markets through dependable logistics coordination and dignified Islamic supplies.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalInk
            )
        }
    }
}

@Composable
private fun MissionCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Our Mission", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Deliver responsive air, sea, customs, door-to-door, and SADC road freight coordination alongside accessible modest clothing, prayer accessories, and educational materials — upheld by honesty, transparency, and respect.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DivisionSummaryCard(
    title: String,
    description: String,
    isGold: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isGold) GoldRich.copy(alpha = 0.5f) else EmeraldDeep.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = if (isGold) GoldRich else EmeraldDeep)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ContactUsScreen(
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onRequestQuote: () -> Unit,
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
            Surface(color = EmeraldDeep, contentColor = PureWhite, modifier = Modifier.fillMaxWidth()) {
                ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, _ ->
                    Column {
                        Text("CONTACT US", style = MaterialTheme.typography.labelMedium, color = GoldBright)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Connect With Our Team",
                            style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Springs, Gauteng, South Africa — serving local, SADC, and international clients.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PureWhite.copy(alpha = 0.90f)
                        )
                    }
                }
            }
        }

        item {
            ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, isTablet ->
                if (isDesktop || isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(22.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OfficialBusinessDetailsCard(settings)
                            ContactDirectActionsRow(onOpenWhatsApp, onRequestQuote)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GoogleMapsLocationCard(settings, onOpenMaps)
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OfficialBusinessDetailsCard(settings)
                        ContactDirectActionsRow(onOpenWhatsApp, onRequestQuote)
                        GoogleMapsLocationCard(settings, onOpenMaps)
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
private fun OfficialBusinessDetailsCard(settings: BusinessSettingsEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, GoldRich.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Official Business Details", style = MaterialTheme.typography.headlineMedium)
            ContactRow("Company", settings.businessName)
            ContactRow("Physical Address", settings.address)
            ContactRow("WhatsApp", settings.whatsappNumber)
            ContactRow(
                "Email",
                settings.emailPlaceholder.ifBlank { "Not configured yet (editable in Admin Settings)" }
            )
            ContactRow(
                "Telephone",
                settings.phonePlaceholder.ifBlank { "Not configured yet (editable in Admin Settings)" }
            )
            if (settings.regNumberPlaceholder.isNotBlank()) {
                ContactRow("Registration No.", settings.regNumberPlaceholder)
            }
            if (settings.vatNumberPlaceholder.isNotBlank()) {
                ContactRow("VAT No.", settings.vatNumberPlaceholder)
            }
        }
    }
}

@Composable
private fun ContactDirectActionsRow(
    onOpenWhatsApp: (String) -> Unit,
    onRequestQuote: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { onOpenWhatsApp(WhatsAppMessages.GENERAL_ENQUIRY) },
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = PureWhite),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .testTag("contact_whatsapp_button")
        ) {
            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("WhatsApp Chat", style = MaterialTheme.typography.labelLarge)
        }
        Button(
            onClick = onRequestQuote,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = PureWhite),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Icon(Icons.Default.RequestQuote, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Request Quote", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun GoogleMapsLocationCard(
    settings: BusinessSettingsEntity,
    onOpenMaps: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = EmeraldSoft),
        border = BorderStroke(1.dp, EmeraldDeep.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldDeep)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Google Maps Location", style = MaterialTheme.typography.headlineMedium, color = EmeraldDeep)
            }
            Text(
                text = settings.address,
                style = MaterialTheme.typography.bodyLarge,
                color = CharcoalInk,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Note: To avoid placing an unverified or inaccurate coordinate pin, tapping below launches a direct Google Maps address search for our Springs, Gauteng street address.",
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalInk
            )
            Button(
                onClick = onOpenMaps,
                colors = ButtonDefaults.buttonColors(containerColor = GoldRich, contentColor = CharcoalInk),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("open_google_maps_button")
            ) {
                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search Address in Google Maps", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun ContactRow(label: String, value: String) {
    Column {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = GoldRich)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun LegalDocumentScreen(
    isPrivacyPolicy: Boolean,
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = if (isPrivacyPolicy) "Privacy Policy (POPIA Template)" else "Terms & Conditions (Template)"
    val sections = if (isPrivacyPolicy) {
        listOf(
            "1. Operator Identity" to "AL-HALAL LOGISTICS N TRANS (${settings.address}, WhatsApp: ${settings.whatsappNumber}) collects personal information submitted voluntarily through quotation enquiries and direct communications.",
            "2. Information Collected" to "Quotation forms collect customer name, optional business name, WhatsApp number, email address, country, cargo origin/destination details, and optional shipment documents.",
            "3. Purpose & POPIA Alignment" to "Information is processed solely to assess logistics quotations, coordinate freight and customs documentation, and respond to Islamic supplies enquiries in alignment with South Africa's Protection of Personal Information Act (POPIA).",
            "4. Data Storage & Security" to "In this build, enquiries are stored locally on-device unless Firebase Cloud Firestore is provisioned by the business owner. No personal information is sold to third parties.",
            "5. Access & Correction" to "Customers may request access to, correction of, or deletion of their enquiry records by contacting AL-HALAL LOGISTICS N TRANS via our official WhatsApp line (+27 74 076 7068)."
        )
    } else {
        listOf(
            "1. Scope of Services" to "AL-HALAL LOGISTICS N TRANS provides international freight forwarding coordination, South African customs clearance coordination, global door-to-door delivery coordination, SADC road freight, and Islamic supplies showcase/enquiry services.",
            "2. Quotations & Pricing" to "Submitted quotation requests do not constitute a binding contract or guaranteed shipping rate. Final terms, pricing, and transit schedules are confirmed in writing after cargo and documentation assessment.",
            "3. Customs & Regulatory Compliance" to "All import, export, and cross-border shipments are subject to applicable South African laws, SARS customs requirements, supporting documentation, and official regulatory approval.",
            "4. Islamic Supplies Enquiries" to "Product listings function as a showcase and enquiry catalogue. Availability, variations, and pricing are confirmed directly with the customer upon enquiry.",
            "5. Governing Law" to "These terms are prepared for operations originating in the Republic of South Africa and should be reviewed by qualified legal counsel before commercial publication."
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 84.dp)
    ) {
        item {
            Surface(color = EmeraldDeep, contentColor = PureWhite, modifier = Modifier.fillMaxWidth()) {
                ResponsiveContainer(maxContentWidth = 1040.dp, verticalPadding = 26.dp) { isDesktop, _ ->
                    Column {
                        Text("LEGAL & COMPLIANCE", style = MaterialTheme.typography.labelMedium, color = GoldBright)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = title,
                            style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                            color = PureWhite
                        )
                    }
                }
            }
        }

        item {
            ResponsiveContainer(maxContentWidth = 1040.dp, verticalPadding = 24.dp) { _, _ ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(
                        color = StatusWarningBg,
                        border = BorderStroke(1.dp, StatusWarning.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = StatusWarning)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Template Notice: This policy document is provided as a structured template for business owner and legal review prior to commercial publication. It does not constitute certified legal compliance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }

                    sections.forEach { (heading, body) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(heading, style = MaterialTheme.typography.titleMedium)
                                Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
