package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.BusinessSettingsEntity
import com.example.data.model.LogisticsServiceType
import com.example.data.model.QuotationFormInput
import com.example.data.model.SeaFreightOption
import com.example.data.repository.QuotationSubmitResult
import com.example.ui.components.CorporateFooterCard
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ExternalIntents
import com.example.util.WhatsAppMessages

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuoteScreen(
    form: QuotationFormInput,
    fieldErrors: Map<String, String>,
    submitResult: QuotationSubmitResult?,
    isSubmitting: Boolean,
    settings: BusinessSettingsEntity,
    onUpdateForm: ((QuotationFormInput) -> QuotationFormInput) -> Unit,
    onSubmit: () -> Unit,
    onResetForm: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var attachmentError by remember { mutableStateOf<String?>(null) }

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val resolver = context.contentResolver
        val mime = resolver.getType(uri).orEmpty()
        val allowedMimes = setOf("application/pdf", "image/png", "image/jpeg", "image/jpg")
        if (mime !in allowedMimes) {
            attachmentError = "Unsupported file type ($mime). Only PDF, PNG, or JPEG allowed."
            return@rememberLauncherForActivityResult
        }
        var displayName = "shipment_document"
        var fileSizeBytes = 0L
        resolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (nameIdx >= 0) displayName = cursor.getString(nameIdx) ?: displayName
                if (sizeIdx >= 0) fileSizeBytes = cursor.getLong(sizeIdx)
            }
        }
        val maxBytes = 5L * 1024L * 1024L
        if (fileSizeBytes > maxBytes) {
            attachmentError = "File exceeds 5 MB limit. Please select a smaller document."
            return@rememberLauncherForActivityResult
        }
        attachmentError = null
        onUpdateForm {
            it.copy(
                attachmentDisplayName = displayName,
                attachmentUriString = uri.toString()
            )
        }
    }

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
                        Text(
                            text = "ADVANCED LOGISTICS QUOTATION SYSTEM",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldBright
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Request a Logistics Quotation",
                            style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Complete the structured shipment details below. No automatic or estimated shipping prices are invented; every enquiry is assessed individually.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PureWhite.copy(alpha = 0.90f),
                            modifier = Modifier.widthIn(max = 820.dp)
                        )
                    }
                }
            }
        }

        if (submitResult != null) {
            item {
                ResponsiveContainer(verticalPadding = 16.dp) { _, _ ->
                    when (submitResult) {
                        is QuotationSubmitResult.ValidationFailed -> {
                            Surface(
                                color = StatusErrorBg,
                                border = BorderStroke(1.dp, StatusError),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = StatusError)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Please complete all required fields highlighted below (${submitResult.fieldErrors.size} issue(s) found).",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = StatusError
                                    )
                                }
                            }
                        }

                        is QuotationSubmitResult.Error -> {
                            Surface(
                                color = StatusErrorBg,
                                border = BorderStroke(1.dp, StatusError),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = submitResult.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = StatusError,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        is QuotationSubmitResult.SavedLocallyCloudConfigRequired -> {
                            val enquiry = submitResult.enquiry
                            val status = submitResult.integrationStatus
                            Card(
                                colors = CardDefaults.cardColors(containerColor = StatusWarningBg),
                                border = BorderStroke(1.5.dp, StatusWarning),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quotation_result_card")
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Saved as Local Draft — Cloud Backend Not Connected",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = CharcoalInk
                                        )
                                    }
                                    Text(
                                        text = "Reference: ${enquiry.referenceNumber}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = EmeraldDeep,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Transparency Notice: Firebase Firestore & automated server-side email notifications are not configured in this build, so this quotation has NOT been transmitted to a cloud database yet. It is saved locally on this device.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CharcoalInk
                                    )
                                    status.requiredActions.forEach { action ->
                                        Text(
                                            text = "• $action",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CharcoalInk
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                onOpenWhatsApp(ExternalIntents.formatQuotationForWhatsApp(enquiry))
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = WhatsAppGreen,
                                                contentColor = PureWhite
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1.4f)
                                                .height(50.dp)
                                                .testTag("quotation_send_whatsapp_btn")
                                        ) {
                                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Send Complete Quotation via WhatsApp Now", style = MaterialTheme.typography.labelLarge)
                                        }
                                        OutlinedButton(
                                            onClick = onResetForm,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(50.dp)
                                        ) {
                                            Text("Start Another Quotation")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Form Sections inside ResponsiveContainer
        item {
            ResponsiveContainer(verticalPadding = 24.dp) { isDesktop, isTablet ->
                val isWide = isDesktop || isTablet
                Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                    // SECTION 1: CUSTOMER DETAILS
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
                            SectionHeader(eyebrow = "Section 1", title = "Customer Details")
                            if (isWide) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    QuoteTextField(
                                        label = "Full Name *",
                                        value = form.fullName,
                                        error = fieldErrors["fullName"],
                                        tag = "quote_full_name",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(fullName = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Business or Company Name (Optional)",
                                        value = form.companyName,
                                        error = null,
                                        tag = "quote_company",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(companyName = v) } }
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    QuoteTextField(
                                        label = "WhatsApp Number *",
                                        value = form.whatsappNumber,
                                        error = fieldErrors["whatsappNumber"],
                                        tag = "quote_whatsapp",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(whatsappNumber = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Email Address *",
                                        value = form.email,
                                        error = fieldErrors["email"],
                                        tag = "quote_email",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(email = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Country *",
                                        value = form.country,
                                        error = fieldErrors["country"],
                                        tag = "quote_country",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(country = v) } }
                                    )
                                }
                            } else {
                                QuoteTextField(
                                    label = "Full Name *",
                                    value = form.fullName,
                                    error = fieldErrors["fullName"],
                                    tag = "quote_full_name",
                                    onValueChange = { v -> onUpdateForm { it.copy(fullName = v) } }
                                )
                                QuoteTextField(
                                    label = "Business or Company Name (Optional)",
                                    value = form.companyName,
                                    error = null,
                                    tag = "quote_company",
                                    onValueChange = { v -> onUpdateForm { it.copy(companyName = v) } }
                                )
                                QuoteTextField(
                                    label = "WhatsApp Number *",
                                    value = form.whatsappNumber,
                                    error = fieldErrors["whatsappNumber"],
                                    tag = "quote_whatsapp",
                                    onValueChange = { v -> onUpdateForm { it.copy(whatsappNumber = v) } }
                                )
                                QuoteTextField(
                                    label = "Email Address *",
                                    value = form.email,
                                    error = fieldErrors["email"],
                                    tag = "quote_email",
                                    onValueChange = { v -> onUpdateForm { it.copy(email = v) } }
                                )
                                QuoteTextField(
                                    label = "Country *",
                                    value = form.country,
                                    error = fieldErrors["country"],
                                    tag = "quote_country",
                                    onValueChange = { v -> onUpdateForm { it.copy(country = v) } }
                                )
                            }
                        }
                    }

                    // SECTION 2: SERVICE TYPE
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SectionHeader(eyebrow = "Section 2", title = "Service Type")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LogisticsServiceType.entries.forEach { svc ->
                                    FilterChip(
                                        selected = form.serviceType == svc,
                                        onClick = { onUpdateForm { it.copy(serviceType = svc) } },
                                        label = { Text(svc.shortTitle) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = EmeraldDeep,
                                            selectedLabelColor = GoldBright
                                        ),
                                        modifier = Modifier.testTag("service_chip_${svc.routeKey}")
                                    )
                                }
                            }

                            if (form.serviceType == LogisticsServiceType.SEA_FREIGHT) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                                    border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.6f)),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("Sea Freight Container Option", style = MaterialTheme.typography.titleSmall)
                                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            SeaFreightOption.entries.forEach { opt ->
                                                FilterChip(
                                                    selected = form.seaFreightOption == opt,
                                                    onClick = { onUpdateForm { it.copy(seaFreightOption = opt) } },
                                                    label = { Text(opt.label) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (form.serviceType == LogisticsServiceType.SADC_ROAD_FREIGHT) {
                                QuoteTextField(
                                    label = "Vehicle or Cargo Requirements (SADC Road Freight)",
                                    value = form.roadVehicleRequirements,
                                    error = null,
                                    tag = "quote_road_req",
                                    singleLine = false,
                                    onValueChange = { v -> onUpdateForm { it.copy(roadVehicleRequirements = v) } }
                                )
                            }
                        }
                    }

                    // SECTION 3: SHIPMENT DETAILS
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
                            SectionHeader(eyebrow = "Section 3", title = "Shipment Details")
                            if (isWide) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    QuoteTextField(
                                        label = "Country of Origin *",
                                        value = form.originCountry,
                                        error = fieldErrors["originCountry"],
                                        tag = "quote_origin_country",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(originCountry = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Collection City *",
                                        value = form.collectionCity,
                                        error = fieldErrors["collectionCity"],
                                        tag = "quote_collection_city",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(collectionCity = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Collection Address *",
                                        value = form.collectionAddress,
                                        error = fieldErrors["collectionAddress"],
                                        tag = "quote_collection_address",
                                        modifier = Modifier.weight(1.3f),
                                        onValueChange = { v -> onUpdateForm { it.copy(collectionAddress = v) } }
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    QuoteTextField(
                                        label = "Destination Country *",
                                        value = form.destinationCountry,
                                        error = fieldErrors["destinationCountry"],
                                        tag = "quote_destination_country",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(destinationCountry = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Delivery City *",
                                        value = form.deliveryCity,
                                        error = fieldErrors["deliveryCity"],
                                        tag = "quote_delivery_city",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(deliveryCity = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Delivery Address *",
                                        value = form.deliveryAddress,
                                        error = fieldErrors["deliveryAddress"],
                                        tag = "quote_delivery_address",
                                        modifier = Modifier.weight(1.3f),
                                        onValueChange = { v -> onUpdateForm { it.copy(deliveryAddress = v) } }
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    QuoteTextField(
                                        label = "Type of Goods *",
                                        value = form.goodsType,
                                        error = fieldErrors["goodsType"],
                                        tag = "quote_goods_type",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(goodsType = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Estimated Cargo Value (Optional)",
                                        value = form.estimatedValue,
                                        error = null,
                                        tag = "quote_value",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(estimatedValue = v) } }
                                    )
                                }
                                QuoteTextField(
                                    label = "Cargo Description *",
                                    value = form.cargoDescription,
                                    error = fieldErrors["cargoDescription"],
                                    tag = "quote_cargo_description",
                                    singleLine = false,
                                    onValueChange = { v -> onUpdateForm { it.copy(cargoDescription = v) } }
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    BoxWeightField(
                                        label = "Estimated Weight *",
                                        value = form.estimatedWeight,
                                        error = fieldErrors["estimatedWeight"],
                                        tag = "quote_weight",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(estimatedWeight = v) } }
                                    )
                                    BoxWeightField(
                                        label = "Packages *",
                                        value = form.numberOfPackages,
                                        error = fieldErrors["numberOfPackages"],
                                        tag = "quote_packages",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(numberOfPackages = v) } }
                                    )
                                    QuoteTextField(
                                        label = "Cargo Dimensions (L x W x H) *",
                                        value = form.cargoDimensions,
                                        error = fieldErrors["cargoDimensions"],
                                        tag = "quote_dimensions",
                                        modifier = Modifier.weight(1.4f),
                                        onValueChange = { v -> onUpdateForm { it.copy(cargoDimensions = v) } }
                                    )
                                }
                            } else {
                                QuoteTextField(
                                    label = "Country of Origin *",
                                    value = form.originCountry,
                                    error = fieldErrors["originCountry"],
                                    tag = "quote_origin_country",
                                    onValueChange = { v -> onUpdateForm { it.copy(originCountry = v) } }
                                )
                                QuoteTextField(
                                    label = "Collection City *",
                                    value = form.collectionCity,
                                    error = fieldErrors["collectionCity"],
                                    tag = "quote_collection_city",
                                    onValueChange = { v -> onUpdateForm { it.copy(collectionCity = v) } }
                                )
                                QuoteTextField(
                                    label = "Collection Address *",
                                    value = form.collectionAddress,
                                    error = fieldErrors["collectionAddress"],
                                    tag = "quote_collection_address",
                                    onValueChange = { v -> onUpdateForm { it.copy(collectionAddress = v) } }
                                )
                                QuoteTextField(
                                    label = "Destination Country *",
                                    value = form.destinationCountry,
                                    error = fieldErrors["destinationCountry"],
                                    tag = "quote_destination_country",
                                    onValueChange = { v -> onUpdateForm { it.copy(destinationCountry = v) } }
                                )
                                QuoteTextField(
                                    label = "Delivery City *",
                                    value = form.deliveryCity,
                                    error = fieldErrors["deliveryCity"],
                                    tag = "quote_delivery_city",
                                    onValueChange = { v -> onUpdateForm { it.copy(deliveryCity = v) } }
                                )
                                QuoteTextField(
                                    label = "Delivery Address *",
                                    value = form.deliveryAddress,
                                    error = fieldErrors["deliveryAddress"],
                                    tag = "quote_delivery_address",
                                    onValueChange = { v -> onUpdateForm { it.copy(deliveryAddress = v) } }
                                )
                                QuoteTextField(
                                    label = "Type of Goods *",
                                    value = form.goodsType,
                                    error = fieldErrors["goodsType"],
                                    tag = "quote_goods_type",
                                    onValueChange = { v -> onUpdateForm { it.copy(goodsType = v) } }
                                )
                                QuoteTextField(
                                    label = "Cargo Description *",
                                    value = form.cargoDescription,
                                    error = fieldErrors["cargoDescription"],
                                    tag = "quote_cargo_description",
                                    singleLine = false,
                                    onValueChange = { v -> onUpdateForm { it.copy(cargoDescription = v) } }
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    BoxWeightField(
                                        label = "Estimated Weight *",
                                        value = form.estimatedWeight,
                                        error = fieldErrors["estimatedWeight"],
                                        tag = "quote_weight",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(estimatedWeight = v) } }
                                    )
                                    BoxWeightField(
                                        label = "Packages *",
                                        value = form.numberOfPackages,
                                        error = fieldErrors["numberOfPackages"],
                                        tag = "quote_packages",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = { v -> onUpdateForm { it.copy(numberOfPackages = v) } }
                                    )
                                }
                                QuoteTextField(
                                    label = "Cargo Dimensions (L x W x H) *",
                                    value = form.cargoDimensions,
                                    error = fieldErrors["cargoDimensions"],
                                    tag = "quote_dimensions",
                                    onValueChange = { v -> onUpdateForm { it.copy(cargoDimensions = v) } }
                                )
                                QuoteTextField(
                                    label = "Estimated Cargo Value (Optional)",
                                    value = form.estimatedValue,
                                    error = null,
                                    tag = "quote_value",
                                    onValueChange = { v -> onUpdateForm { it.copy(estimatedValue = v) } }
                                )
                            }
                        }
                    }

                    // SECTION 4: ADDITIONAL INFORMATION, ATTACHMENT & CONSENT
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
                            SectionHeader(eyebrow = "Section 4", title = "Additional Information")
                            if (isWide) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    QuoteTextField(
                                        label = "Special Instructions",
                                        value = form.specialInstructions,
                                        error = null,
                                        tag = "quote_instructions",
                                        singleLine = false,
                                        modifier = Modifier.weight(1.2f),
                                        onValueChange = { v -> onUpdateForm { it.copy(specialInstructions = v) } }
                                    )
                                    AttachmentPickerCard(
                                        displayName = form.attachmentDisplayName,
                                        attachmentError = attachmentError,
                                        onPick = {
                                            documentPickerLauncher.launch(arrayOf("application/pdf", "image/png", "image/jpeg"))
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                QuoteTextField(
                                    label = "Special Instructions",
                                    value = form.specialInstructions,
                                    error = null,
                                    tag = "quote_instructions",
                                    singleLine = false,
                                    onValueChange = { v -> onUpdateForm { it.copy(specialInstructions = v) } }
                                )
                                AttachmentPickerCard(
                                    displayName = form.attachmentDisplayName,
                                    attachmentError = attachmentError,
                                    onPick = {
                                        documentPickerLauncher.launch(arrayOf("application/pdf", "image/png", "image/jpeg"))
                                    }
                                )
                            }

                            // POPIA Privacy Consent
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onUpdateForm { it.copy(privacyConsentAccepted = !it.privacyConsentAccepted) }
                                        }
                                ) {
                                    Checkbox(
                                        checked = form.privacyConsentAccepted,
                                        onCheckedChange = { checked ->
                                            onUpdateForm { it.copy(privacyConsentAccepted = checked) }
                                        },
                                        modifier = Modifier.testTag("quote_privacy_checkbox")
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "I consent to AL-HALAL LOGISTICS N TRANS processing my enquiry details in accordance with the Privacy Policy (POPIA-aligned notice).",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "Read Privacy Policy",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldRich,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { onNavigate(AppRoutes.ROUTE_PRIVACY_POLICY) }
                                                .padding(vertical = 4.dp)
                                        )
                                    }
                                }
                                fieldErrors["privacyConsent"]?.let { err ->
                                    Text(
                                        text = err,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = StatusError,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = onSubmit,
                                enabled = !isSubmitting,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldRich,
                                    contentColor = CharcoalInk
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .widthIn(max = if (isWide) 360.dp else 1000.dp)
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .testTag("submit_quotation_button")
                            ) {
                                if (isSubmitting) {
                                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.RequestQuote, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("REQUEST MY QUOTATION", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
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
private fun AttachmentPickerCard(
    displayName: String,
    attachmentError: String?,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Optional Packing List / Invoice Attachment", style = MaterialTheme.typography.titleSmall)
            Text(
                text = "Accepted formats: PDF, PNG, JPEG (max 5 MB). Stored on-device only until cloud storage is configured.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (displayName.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldDeep, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(displayName, style = MaterialTheme.typography.bodySmall)
                }
            }
            if (!attachmentError.isNullOrBlank()) {
                Text(attachmentError, style = MaterialTheme.typography.bodySmall, color = StatusError)
            }
            OutlinedButton(
                onClick = onPick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (displayName.isBlank()) "Attach Document" else "Replace Document")
            }
        }
    }
}

@Composable
private fun QuoteTextField(
    label: String,
    value: String,
    error: String?,
    tag: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag(tag)
    )
}

@Composable
private fun BoxWeightField(
    label: String,
    value: String,
    error: String?,
    tag: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag(tag)
    )
}
