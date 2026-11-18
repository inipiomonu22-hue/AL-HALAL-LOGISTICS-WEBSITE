package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.CategoryEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.model.BackendIntegrationStatus
import com.example.data.model.EnquiryStatus
import com.example.data.model.LogisticsServiceType
import com.example.data.model.ProductCategoryType
import com.example.data.model.SadcCountryConfig
import com.example.ui.components.ProductOrBannerImage
import com.example.ui.components.ResponsiveContainer
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.util.ExternalIntents

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardScreen(
    isSandboxEnabled: Boolean,
    integrationStatus: BackendIntegrationStatus,
    enquiries: List<EnquiryEntity>,
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    settings: BusinessSettingsEntity,
    sadcCountries: List<SadcCountryConfig>,
    onSetSandboxEnabled: (Boolean) -> Unit,
    onUpdateEnquiryStatus: (Long, EnquiryStatus) -> Unit,
    onSaveProduct: (ProductEntity) -> Unit,
    onToggleProductVisibility: (ProductEntity) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onSaveCategory: (CategoryEntity) -> Unit,
    onSaveBusinessSettings: (BusinessSettingsEntity) -> Unit,
    onToggleSadcCountry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (!isSandboxEnabled) {
        // Protected Gate — never exposes public admin actions or hardcoded password
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("admin_gate_list"),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ResponsiveContainer(maxContentWidth = 1040.dp) { _, _ ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = EmeraldDeep),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = GoldBright)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Protected Administration Dashboard",
                                style = MaterialTheme.typography.headlineMedium,
                                color = PureWhite
                            )
                        }
                        Text(
                            text = "Administrative functions are locked by default. Cloud authentication (Firebase Auth with Google Sign-In) and Firestore are not provisioned in this build.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureWhite.copy(alpha = 0.88f)
                        )
                        }
                    }
                }
            }

            item {
                ResponsiveContainer(maxContentWidth = 1040.dp) { _, _ ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StatusWarningBg),
                        border = BorderStroke(1.dp, StatusWarning),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Administrator Setup Instructions (Production)",
                                style = MaterialTheme.typography.titleMedium,
                                color = CharcoalInk
                            )
                            Text(
                                text = "1. Provision Firebase Firestore and Firebase Auth (Google Sign-In via Credential Manager).\n2. Add authorized admin UIDs to the `/admins/{uid}` collection in Firestore.\n3. Deploy the included `firestore.rules` (enforcing `isAdmin()` for enquiries, products, categories, and settings).\n4. Configure a server-side Cloud Function / SMTP API webhook and notification email address.\n\nNo public default admin password is created in code.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }
                }
            }

            item {
                ResponsiveContainer(maxContentWidth = 1040.dp) { _, _ ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Local Device Sandbox Preview",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "For evaluation and offline UI testing on this device only, you may enable Local Sandbox Mode. Changes affect only this device's local Room database and do not modify any cloud backend.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { onSetSandboxEnabled(true) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldRich,
                                    contentColor = CharcoalInk
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .widthIn(max = 360.dp)
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("enable_admin_sandbox_btn")
                            ) {
                                Text("Enable Local Sandbox Preview", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // Active Admin Sandbox UI
    val tabs = listOf(
        "Overview",
        "Enquiries & Quotes",
        "Products",
        "Categories",
        "Contact & Socials",
        "Website Content",
        "Backend Setup"
    )
    var selectedTab by remember { mutableIntStateOf(0) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var showProductDialog by remember { mutableStateOf(false) }

    if (showProductDialog) {
        ProductEditorDialog(
            initial = editingProduct,
            categories = categories,
            onDismiss = { showProductDialog = false },
            onSave = { product ->
                onSaveProduct(product)
                showProductDialog = false
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Full-Width Sandbox Banner
            Surface(
                color = StatusWarningBg,
                border = BorderStroke(1.dp, StatusWarning.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 1320.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Local Sandbox Mode (No Cloud Sync)",
                                style = MaterialTheme.typography.labelSmall,
                                color = CharcoalInk
                            )
                        }
                        TextButton(
                            onClick = { onSetSandboxEnabled(false) },
                            modifier = Modifier.testTag("exit_admin_sandbox_btn")
                        ) {
                            Text("Lock", color = StatusError, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1320.dp)
                    .weight(1f)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tabs.indices.toList()) { index ->
                        FilterChip(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            label = { Text(tabs[index]) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldDeep,
                                selectedLabelColor = GoldBright
                            ),
                            modifier = Modifier.testTag("admin_tab_$index")
                        )
                    }
                }

        when (selectedTab) {
            0 -> AdminOverviewTab(
                enquiries = enquiries,
                products = products,
                categories = categories,
                integrationStatus = integrationStatus
            )
            1 -> AdminEnquiriesTab(
                enquiries = enquiries,
                onUpdateStatus = onUpdateEnquiryStatus,
                onExportCsv = {
                    val csv = ExternalIntents.buildCsvForEnquiries(enquiries)
                    ExternalIntents.shareCsvText(
                        context = context,
                        csvContent = csv,
                        subject = "AL-HALAL Enquiries Export"
                    )
                }
            )
            2 -> AdminProductsTab(
                products = products,
                categories = categories,
                onAddClick = {
                    editingProduct = null
                    showProductDialog = true
                },
                onEditClick = { p ->
                    editingProduct = p
                    showProductDialog = true
                },
                onToggleVisibility = onToggleProductVisibility,
                onDelete = onDeleteProduct
            )
            3 -> AdminCategoriesTab(
                categories = categories,
                onSaveCategory = onSaveCategory
            )
            4 -> AdminContactSettingsTab(
                settings = settings,
                onSaveSettings = onSaveBusinessSettings
            )
            5 -> AdminWebsiteContentTab(
                settings = settings,
                sadcCountries = sadcCountries,
                onSaveSettings = onSaveBusinessSettings,
                onToggleSadcCountry = onToggleSadcCountry
            )
                6 -> AdminBackendSetupTab(integrationStatus = integrationStatus)
            }
        }
    }
}
}

@Composable
private fun AdminOverviewTab(
    enquiries: List<EnquiryEntity>,
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    integrationStatus: BackendIntegrationStatus
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricBox("Local Enquiries", "${enquiries.size}", Modifier.weight(1f))
                MetricBox("Products", "${products.size}", Modifier.weight(1f))
                MetricBox("Categories", "${categories.size}", Modifier.weight(1f))
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Integration Health Check", style = MaterialTheme.typography.titleMedium)
                    Text("Firebase Firestore: ${if (integrationStatus.firebaseFirestoreConfigured) "Configured" else "Not Configured"}")
                    Text("Firebase Auth (Admin): ${if (integrationStatus.firebaseAuthConfigured) "Configured" else "Not Configured"}")
                    Text("Email Notification Dispatch: ${if (integrationStatus.emailNotificationConfigured) "Configured" else "Not Configured"}")
                    Text(
                        text = "Notification Target: ${integrationStatus.configuredNotificationEmail.ifBlank { "Not configured" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = EmeraldSoft),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineLarge, color = EmeraldDeep)
            Text(label, style = MaterialTheme.typography.labelSmall, color = CharcoalInk)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdminEnquiriesTab(
    enquiries: List<EnquiryEntity>,
    onUpdateStatus: (Long, EnquiryStatus) -> Unit,
    onExportCsv: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    var serviceFilter by remember { mutableStateOf<String?>(null) }

    val filtered = enquiries.filter { e ->
        val matchesSearch = search.isBlank() ||
            e.fullName.contains(search, ignoreCase = true) ||
            e.referenceNumber.contains(search, ignoreCase = true) ||
            e.destinationCountry.contains(search, ignoreCase = true)
        val matchesService = serviceFilter == null || e.serviceType == serviceFilter
        matchesSearch && matchesService
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Search enquiries by name, ref, or destination") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = serviceFilter == null,
                    onClick = { serviceFilter = null },
                    label = { Text("All") }
                )
                LogisticsServiceType.entries.forEach { svc ->
                    FilterChip(
                        selected = serviceFilter == svc.title,
                        onClick = { serviceFilter = svc.title },
                        label = { Text(svc.shortTitle) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onExportCsv,
                enabled = enquiries.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("export_csv_button")
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export Enquiries to CSV (${enquiries.size})")
            }
        }

        if (filtered.isEmpty()) {
            item {
                Text(
                    text = "No enquiry records yet. Submit a quotation in the Quote screen to verify records.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filtered, key = { it.id }) { e ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(e.referenceNumber, style = MaterialTheme.typography.titleSmall, color = EmeraldDeep)
                            Text(e.createdAtIso.take(10), style = MaterialTheme.typography.labelSmall)
                        }
                        Text("${e.fullName} • ${e.whatsappNumber}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("${e.serviceType} | ${e.collectionCity}, ${e.originCountry} → ${e.deliveryCity}, ${e.destinationCountry}", style = MaterialTheme.typography.bodySmall)
                        Text("Cargo: ${e.goodsType} (${e.estimatedWeight}, ${e.numberOfPackages} pkgs)", style = MaterialTheme.typography.bodySmall)
                        Text("Sync Status: ${e.cloudSyncState}", style = MaterialTheme.typography.labelSmall, color = StatusWarning)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            EnquiryStatus.entries.forEach { st ->
                                FilterChip(
                                    selected = e.status == st.label,
                                    onClick = { onUpdateStatus(e.id, st) },
                                    label = { Text(st.label, style = MaterialTheme.typography.labelSmall) }
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
private fun AdminProductsTab(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    onAddClick: () -> Unit,
    onEditClick: (ProductEntity) -> Unit,
    onToggleVisibility: (ProductEntity) -> Unit,
    onDelete: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldRich, contentColor = CharcoalInk),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_add_product_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add New Islamic Product", style = MaterialTheme.typography.labelLarge)
            }
        }

        items(products, key = { "${it.id}_${it.name}" }) { product ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductOrBannerImage(
                        drawableKey = product.imageDrawableKey,
                        customUri = product.customImageUri,
                        contentDescription = product.name,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = categories.firstOrNull { it.id == product.categoryId }?.name ?: product.categoryId,
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldRich
                        )
                        Text(
                            text = if (product.isVisible) "Visible in Catalogue" else "Hidden",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (product.isVisible) EmeraldDeep else StatusWarning
                        )
                    }
                    IconButton(onClick = { onToggleVisibility(product) }) {
                        Icon(
                            imageVector = if (product.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle visibility"
                        )
                    }
                    IconButton(onClick = { onEditClick(product) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit product")
                    }
                    IconButton(onClick = { onDelete(product.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete product", tint = StatusError)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProductEditorDialog(
    initial: ProductEntity?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var categoryId by remember { mutableStateOf(initial?.categoryId ?: ProductCategoryType.MENS_CLOTHING.id) }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var variations by remember { mutableStateOf(initial?.variations ?: "") }
    var suppliedPrice by remember { mutableStateOf(initial?.suppliedPriceText ?: "") }
    var customImageUri by remember { mutableStateOf(initial?.customImageUri ?: "") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            customImageUri = uri.toString()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Product" else "Edit Product") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_product_name_input")
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Category", style = MaterialTheme.typography.labelMedium)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { c ->
                            FilterChip(
                                selected = categoryId == c.id,
                                onClick = { categoryId = c.id },
                                label = { Text(c.name, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description *") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth().testTag("admin_product_desc_input")
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = variations,
                        onValueChange = { variations = it },
                        label = { Text("Available Variations (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = suppliedPrice,
                        onValueChange = { suppliedPrice = it },
                        label = { Text("Verified Price (Leave blank if enquiry-only)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (customImageUri.isBlank()) "Select Product Photo" else "Photo Selected (Change)")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank() && description.isNotBlank(),
                onClick = {
                    val drawableKey = if (categoryId == ProductCategoryType.ISLAMIC_ACCESSORIES.id ||
                        categoryId == ProductCategoryType.BOOKS_AND_EDUCATION.id
                    ) "accessories" else "clothing"
                    onSave(
                        ProductEntity(
                            id = initial?.id ?: 0L,
                            name = name.trim(),
                            categoryId = categoryId,
                            description = description.trim(),
                            variations = variations.trim(),
                            suppliedPriceText = suppliedPrice.trim(),
                            imageDrawableKey = initial?.imageDrawableKey ?: drawableKey,
                            customImageUri = customImageUri,
                            isVisible = initial?.isVisible ?: true,
                            createdAtEpochMs = initial?.createdAtEpochMs ?: System.currentTimeMillis()
                        )
                    )
                },
                modifier = Modifier.testTag("admin_save_product_btn")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AdminCategoriesTab(
    categories: List<CategoryEntity>,
    onSaveCategory: (CategoryEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories, key = { it.id }) { cat ->
            var title by remember(cat.id, cat.name) { mutableStateOf(cat.name) }
            var desc by remember(cat.id, cat.description) { mutableStateOf(cat.description) }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Category Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { onSaveCategory(cat.copy(name = title.trim(), description = desc.trim())) }) {
                        Text("Save Category")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminContactSettingsTab(
    settings: BusinessSettingsEntity,
    onSaveSettings: (BusinessSettingsEntity) -> Unit
) {
    var whatsapp by remember(settings) { mutableStateOf(settings.whatsappNumber) }
    var address by remember(settings) { mutableStateOf(settings.address) }
    var email by remember(settings) { mutableStateOf(settings.emailPlaceholder) }
    var phone by remember(settings) { mutableStateOf(settings.phonePlaceholder) }
    var regNo by remember(settings) { mutableStateOf(settings.regNumberPlaceholder) }
    var vatNo by remember(settings) { mutableStateOf(settings.vatNumberPlaceholder) }
    var notifEmail by remember(settings) { mutableStateOf(settings.notificationEmailPlaceholder) }
    var ig by remember(settings) { mutableStateOf(settings.instagramPlaceholder) }
    var fb by remember(settings) { mutableStateOf(settings.facebookPlaceholder) }
    var li by remember(settings) { mutableStateOf(settings.linkedinPlaceholder) }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("Official WhatsApp") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Physical Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Public Business Email (Optional Placeholder)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telephone (Optional Placeholder)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = regNo, onValueChange = { regNo = it }, label = { Text("Company Registration No. (Optional)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = vatNo, onValueChange = { vatNo = it }, label = { Text("VAT No. (Optional)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = notifEmail, onValueChange = { notifEmail = it }, label = { Text("Admin Enquiry Notification Email (For Server Dispatch)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = ig, onValueChange = { ig = it }, label = { Text("Instagram Handle/URL (Placeholder)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = fb, onValueChange = { fb = it }, label = { Text("Facebook Page URL (Placeholder)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = li, onValueChange = { li = it }, label = { Text("LinkedIn Page URL (Placeholder)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    onSaveSettings(
                        settings.copy(
                            whatsappNumber = whatsapp.trim(),
                            address = address.trim(),
                            emailPlaceholder = email.trim(),
                            phonePlaceholder = phone.trim(),
                            regNumberPlaceholder = regNo.trim(),
                            vatNumberPlaceholder = vatNo.trim(),
                            notificationEmailPlaceholder = notifEmail.trim(),
                            instagramPlaceholder = ig.trim(),
                            facebookPlaceholder = fb.trim(),
                            linkedinPlaceholder = li.trim()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Save Contact & Placeholders")
            }
        }
    }
}

@Composable
private fun AdminWebsiteContentTab(
    settings: BusinessSettingsEntity,
    sadcCountries: List<SadcCountryConfig>,
    onSaveSettings: (BusinessSettingsEntity) -> Unit,
    onToggleSadcCountry: (String) -> Unit
) {
    var heroHeadline by remember(settings) { mutableStateOf(settings.heroHeadline) }
    var heroSub by remember(settings) { mutableStateOf(settings.heroSubheading) }
    var aboutText by remember(settings) { mutableStateOf(settings.aboutDescription) }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(value = heroHeadline, onValueChange = { heroHeadline = it }, label = { Text("Hero Heading") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = heroSub, onValueChange = { heroSub = it }, label = { Text("Hero Supporting Text") }, minLines = 3, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = aboutText, onValueChange = { aboutText = it }, label = { Text("About Us Description") }, minLines = 4, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    onSaveSettings(
                        settings.copy(
                            heroHeadline = heroHeadline.trim(),
                            heroSubheading = heroSub.trim(),
                            aboutDescription = aboutText.trim()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Content")
            }
        }

        item {
            Text("SADC Country Availability Toggles", style = MaterialTheme.typography.titleMedium)
        }
        items(sadcCountries, key = { it.code }) { country ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(country.name, style = MaterialTheme.typography.titleSmall)
                    Text(country.corridorNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = country.isServiceEnabled,
                    onCheckedChange = { onToggleSadcCountry(country.code) }
                )
            }
        }
    }
}

@Composable
private fun AdminBackendSetupTab(integrationStatus: BackendIntegrationStatus) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Required External Configuration", style = MaterialTheme.typography.titleMedium)
                    integrationStatus.requiredActions.forEach { req ->
                        Text("• $req", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Deployment Checklist:\n1. Enable Firebase setup in Google AI Studio (or place `google-services.json` and `firebase_applet_config.xml`).\n2. Enable Google Sign-In in Firebase Auth & add admin UIDs under `/admins/{uid}`.\n3. Deploy `/firestore.rules` (template included at project root) so `enquiries` are unreadable by non-owners/public users.\n4. Connect a server-side email service (Firebase Cloud Functions + SendGrid/Postmark/Resend) using server environment secrets (`EMAIL_API_KEY`, `NOTIFICATION_EMAIL`). Never store email secrets in the Android APK.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
