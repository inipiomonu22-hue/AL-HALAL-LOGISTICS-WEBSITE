package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.BusinessSettingsEntity
import com.example.data.local.CategoryEntity
import com.example.data.local.ProductEntity
import com.example.data.model.ProductCategoryType
import com.example.ui.components.CorporateFooterCard
import com.example.ui.components.DesktopBreakpoint
import com.example.ui.components.IslamicGeometricBackground
import com.example.ui.components.ProductOrBannerImage
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.ResponsiveGrid
import com.example.ui.components.SectionHeader
import com.example.ui.components.TabletBreakpoint
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.CharcoalInk
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.GoldSoft
import com.example.ui.theme.PureWhite
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppMessages

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IslamicSuppliesOverviewScreen(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    searchQuery: String,
    selectedCategoryId: String?,
    settings: BusinessSettingsEntity,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onNavigate: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isDesktop = maxWidth >= DesktopBreakpoint
        val isTablet = maxWidth >= TabletBreakpoint && maxWidth < DesktopBreakpoint
        val gridCols = when {
            isDesktop -> 3
            isTablet -> 2
            else -> 1
        }
        val productRows = products.chunked(gridCols)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("islamic_catalogue_list"),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            // Distinct Division Two Full-Width Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(EmeraldDeep, EmeraldDeep.copy(alpha = 0.92f)))
                        )
                ) {
                    IslamicGeometricBackground(
                        modifier = Modifier.matchParentSize(),
                        lineColor = GoldRich.copy(alpha = 0.14f)
                    )
                    ResponsiveContainer(verticalPadding = if (isDesktop) 32.dp else 18.dp) { desktop, _ ->
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "DIVISION TWO • SHOWCASE & ENQUIRY CATALOGUE",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldBright
                            )
                            Text(
                                text = "AL-HALAL ISLAMIC SUPPLIES",
                                style = if (desktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.headlineLarge,
                                color = PureWhite
                            )
                            Text(
                                text = "Modest Islamic clothing, prayer mats, tasbeeh, educational books, and Islamic lifestyle supplies — available for international enquiry.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PureWhite.copy(alpha = 0.90f),
                                modifier = Modifier.widthIn(max = 760.dp)
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onNavigate(AppRoutes.ROUTE_MENS_CLOTHING) },
                                    border = BorderStroke(1.dp, GoldBright),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldBright)
                                ) {
                                    Text("Men's Clothing", style = MaterialTheme.typography.labelSmall)
                                }
                                OutlinedButton(
                                    onClick = { onNavigate(AppRoutes.ROUTE_WOMENS_CLOTHING) },
                                    border = BorderStroke(1.dp, GoldBright),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldBright)
                                ) {
                                    Text("Women's Clothing", style = MaterialTheme.typography.labelSmall)
                                }
                                OutlinedButton(
                                    onClick = { onNavigate(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES) },
                                    border = BorderStroke(1.dp, GoldBright),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldBright)
                                ) {
                                    Text("Accessories & Books", style = MaterialTheme.typography.labelSmall)
                                }
                                OutlinedButton(
                                    onClick = { onNavigate(AppRoutes.ROUTE_ISLAMIC_GUIDANCE) },
                                    border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.7f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite)
                                ) {
                                    Text("Islamic Guidance", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            // Search & Filter Controls
            item {
                ResponsiveContainer(verticalPadding = 16.dp) { desktop, tablet ->
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (desktop || tablet) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchQueryChange,
                                    placeholder = { Text("Search thobes, abayas, hijabs, prayer mats...") },
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { onSearchQueryChange("") }) {
                                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("product_search_input")
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    CategoryFilterChips(
                                        categories = categories,
                                        selectedCategoryId = selectedCategoryId,
                                        onSelectCategory = onSelectCategory
                                    )
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                placeholder = { Text("Search thobes, abayas, hijabs, prayer mats...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onSearchQueryChange("") }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("product_search_input")
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CategoryFilterChips(
                                    categories = categories,
                                    selectedCategoryId = selectedCategoryId,
                                    onSelectCategory = onSelectCategory
                                )
                            }
                        }

                        Surface(
                            color = GoldSoft,
                            border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Product Showcase & Enquiry Catalogue: Items listed below are open for direct WhatsApp and bulk supply enquiries. Pricing and stock availability are confirmed directly upon enquiry.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            }

            if (products.isEmpty()) {
                item {
                    ResponsiveContainer(verticalPadding = 16.dp) { _, _ ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = GoldRich,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = "No matching products found",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Clear your filter or message us directly on WhatsApp for custom Islamic supplies sourcing.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Button(
                                    onClick = { onOpenWhatsApp(WhatsAppMessages.ISLAMIC_PRODUCTS) },
                                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
                                ) {
                                    Text("Enquire on WhatsApp")
                                }
                            }
                        }
                    }
                }
            } else {
                items(
                    items = productRows,
                    key = { row -> row.joinToString("|") { "${it.id}_${it.name}" } }
                ) { rowItems ->
                    ResponsiveContainer(verticalPadding = 8.dp) { _, _ ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowItems.forEach { product ->
                                ProductCatalogueCard(
                                    product = product,
                                    categoryName = categories.firstOrNull { it.id == product.categoryId }?.name
                                        ?: ProductCategoryType.fromIdOrDefault(product.categoryId).displayName,
                                    onEnquire = { onOpenWhatsApp(WhatsAppMessages.productEnquiry(product.name)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(gridCols - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                CorporateFooterCard(
                    settings = settings,
                    onNavigate = onNavigate,
                    onOpenWhatsApp = { onOpenWhatsApp(WhatsAppMessages.ISLAMIC_PRODUCTS) },
                    onOpenMaps = onOpenMaps
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterChips(
    categories: List<CategoryEntity>,
    selectedCategoryId: String?,
    onSelectCategory: (String?) -> Unit
) {
    FilterChip(
        selected = selectedCategoryId == null,
        onClick = { onSelectCategory(null) },
        label = { Text("All Categories") },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldDeep,
            selectedLabelColor = GoldBright
        ),
        modifier = Modifier.testTag("filter_all")
    )
    categories.forEach { cat ->
        FilterChip(
            selected = selectedCategoryId == cat.id,
            onClick = { onSelectCategory(cat.id) },
            label = { Text(cat.name) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EmeraldDeep,
                selectedLabelColor = GoldBright
            ),
            modifier = Modifier.testTag("filter_${cat.id}")
        )
    }
}

@Composable
fun IslamicCategoryPageScreen(
    categoryType: ProductCategoryType,
    products: List<ProductEntity>,
    settings: BusinessSettingsEntity,
    onNavigate: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    onOpenMaps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryProducts = products.filter {
        if (categoryType == ProductCategoryType.ISLAMIC_ACCESSORIES) {
            it.categoryId == ProductCategoryType.ISLAMIC_ACCESSORIES.id ||
                it.categoryId == ProductCategoryType.BOOKS_AND_EDUCATION.id
        } else {
            it.categoryId == categoryType.id
        }
    }

    val highlights = when (categoryType) {
        ProductCategoryType.MENS_CLOTHING -> listOf(
            "Thobes — classic & tailored everyday silhouettes",
            "Jubbas — formal & seasonal traditional styles",
            "Kufi caps & prayer caps",
            "Traditional Islamic garments",
            "Men's modest clothing accessories"
        )
        ProductCategoryType.WOMENS_CLOTHING -> listOf(
            "Abayas — flowing, full-coverage modest designs",
            "Hijabs — breathable chiffon, jersey & silk-touch wraps",
            "Khimars — generous modest head & shoulder coverage",
            "Jilbabs & modest dresses",
            "Islamic women's modest accessories"
        )
        else -> listOf(
            "Prayer mats — plush woven & travel-friendly options",
            "Tasbeeh — 33-bead and 99-bead prayer beads",
            "Islamic decorative items & home accents",
            "Prayer accessories & Islamic lifestyle products",
            "Islamic books & educational learning materials"
        )
    }

    val bannerDrawable = if (categoryType == ProductCategoryType.ISLAMIC_ACCESSORIES ||
        categoryType == ProductCategoryType.BOOKS_AND_EDUCATION
    ) "accessories" else "clothing"

    val defaultWhatsApp = when (categoryType) {
        ProductCategoryType.MENS_CLOTHING,
        ProductCategoryType.WOMENS_CLOTHING -> WhatsAppMessages.ISLAMIC_CLOTHING
        else -> WhatsAppMessages.ISLAMIC_PRODUCTS
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isDesktop = maxWidth >= DesktopBreakpoint
        val isTablet = maxWidth >= TabletBreakpoint && maxWidth < DesktopBreakpoint
        val gridCols = when {
            isDesktop -> 3
            isTablet -> 2
            else -> 1
        }
        val productRows = categoryProducts.chunked(gridCols)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    ProductOrBannerImage(
                        drawableKey = bannerDrawable,
                        contentDescription = categoryType.displayName,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(EmeraldDeep.copy(alpha = 0.80f))
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        ResponsiveContainer(verticalPadding = 24.dp) { desktop, _ ->
                            Column {
                                Text(
                                    text = "AL-HALAL ISLAMIC SUPPLIES",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GoldBright
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = categoryType.displayName,
                                    style = if (desktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                                    color = PureWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = categoryType.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PureWhite.copy(alpha = 0.92f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                ResponsiveContainer(verticalPadding = 24.dp) { desktop, tablet ->
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        if (desktop || tablet) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(18.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                CategoryHighlightsCard(
                                    highlights = highlights,
                                    modifier = Modifier.weight(1.2f)
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    if (categoryType == ProductCategoryType.ISLAMIC_ACCESSORIES) {
                                        IslamicBooksHighlightCard()
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = GoldSoft),
                                        border = BorderStroke(1.dp, GoldRich),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(18.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                text = "Direct Category Enquiry",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = CharcoalInk
                                            )
                                            Text(
                                                text = "Need sizing, wholesale quantities, or shipping across South Africa and internationally?",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CharcoalInk
                                            )
                                            Button(
                                                onClick = { onOpenWhatsApp(defaultWhatsApp) },
                                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = PureWhite),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(48.dp)
                                                    .testTag("category_whatsapp_button")
                                            ) {
                                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Enquire About ${categoryType.displayName} on WhatsApp", style = MaterialTheme.typography.labelLarge)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            CategoryHighlightsCard(highlights = highlights)
                            if (categoryType == ProductCategoryType.ISLAMIC_ACCESSORIES) {
                                IslamicBooksHighlightCard()
                            }
                            Button(
                                onClick = { onOpenWhatsApp(defaultWhatsApp) },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = PureWhite),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("category_whatsapp_button")
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Enquire About ${categoryType.displayName} on WhatsApp", style = MaterialTheme.typography.labelLarge)
                            }
                        }

                        SectionHeader(
                            eyebrow = "Catalogue Selection",
                            title = "Featured Items"
                        )
                    }
                }
            }

            items(
                items = productRows,
                key = { row -> row.joinToString("|") { "${it.id}_${it.name}" } }
            ) { rowItems ->
                ResponsiveContainer(verticalPadding = 8.dp) { _, _ ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { product ->
                            ProductCatalogueCard(
                                product = product,
                                categoryName = ProductCategoryType.fromIdOrDefault(product.categoryId).displayName,
                                onEnquire = { onOpenWhatsApp(WhatsAppMessages.productEnquiry(product.name)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(gridCols - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                CorporateFooterCard(
                    settings = settings,
                    onNavigate = onNavigate,
                    onOpenWhatsApp = { onOpenWhatsApp(defaultWhatsApp) },
                    onOpenMaps = onOpenMaps
                )
            }
        }
    }
}

@Composable
private fun CategoryHighlightsCard(
    highlights: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = EmeraldSoft),
        border = BorderStroke(1.dp, GoldRich.copy(alpha = 0.45f)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Checkroom, contentDescription = null, tint = EmeraldDeep)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Available Categories & Items", style = MaterialTheme.typography.titleMedium, color = EmeraldDeep)
            }
            highlights.forEach { itemText ->
                Text("• $itemText", style = MaterialTheme.typography.bodyMedium, color = CharcoalInk)
            }
        }
    }
}

@Composable
private fun IslamicBooksHighlightCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, GoldRich),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = GoldRich)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Islamic Books & Educational Materials", style = MaterialTheme.typography.headlineMedium)
            }
            Text(
                text = "We also coordinate sourcing and supply of Islamic books, Arabic & Tajweed workbooks, Seerah study resources, and educational materials for families, schools, and community libraries.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProductCatalogueCard(
    product: ProductEntity,
    categoryName: String,
    onEnquire: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            ProductOrBannerImage(
                drawableKey = product.imageDrawableKey,
                customUri = product.customImageUri,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = EmeraldSoft,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldDeep,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (product.variations.isNotBlank()) {
                    Text(
                        text = "Variations: ${product.variations}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = if (product.suppliedPriceText.isNotBlank()) {
                        "Supplied Price: ${product.suppliedPriceText}"
                    } else {
                        "Availability & Pricing: Confirmed on enquiry"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldRich,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onEnquire,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("product_enquire_${product.id}")
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enquire on WhatsApp", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun IslamicGuidanceScreen(
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
                    lineColor = GoldRich.copy(alpha = 0.14f)
                )
                ResponsiveContainer(verticalPadding = 28.dp) { isDesktop, _ ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "EDUCATIONAL PRODUCT GUIDE",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldBright
                        )
                        Text(
                            text = "Islamic Product & Modest Dressing Guidance",
                            style = if (isDesktop) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                            color = PureWhite
                        )
                        Text(
                            text = "Helpful, respectful guidance for selecting modest Islamic garments, prayer mats, tasbeeh, and educational supplies.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PureWhite.copy(alpha = 0.90f),
                            modifier = Modifier.widthIn(max = 780.dp)
                        )
                    }
                }
            }
        }

        item {
            ResponsiveContainer(verticalPadding = 24.dp) { isDesktop, isTablet ->
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
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
                                contentDescription = "Guidance Disclaimer",
                                tint = StatusWarning,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Important Disclaimer: This section provides general product and modest lifestyle guidance only. It does not constitute formal fatwas or professional religious rulings. For specific fiqh rulings, please consult a qualified Islamic scholar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalInk
                            )
                        }
                    }

                    val guides = listOf(
                        "Modest Dressing Principles" to "Across Islamic tradition, modest dress emphasises dignity, cleanliness, and appropriate coverage (awrah) for men and women without excessive ostentation. Loose-fitting, non-transparent fabrics are broadly preferred for daily wear and prayer.",
                        "Men's Islamic Garments (Thobes, Jubbas & Kufi Caps)" to "When selecting a thobe or jubba, consider climate-appropriate breathable cotton or linen blends for South African summers and heavier woven fabrics for cooler months. Ensure comfortable shoulder width and modest length above the ankles where preferred.",
                        "Women's Modest Fashion (Abayas, Hijabs, Khimars & Jilbabs)" to "Look for opaque, non-clingy drapes such as nidha, crepe, or soft linen for abayas and jilbabs. For hijabs and khimars, breathable chiffon, modal, or cotton-jersey fabrics paired with an undercap help maintain secure, comfortable coverage.",
                        "Prayer Accessories (Prayer Mats & Tasbeeh)" to "A clean, dedicated prayer mat (sajjadah) supports focus and hygiene at home, work, or during travel. Padded mats assist comfort on hard floors, while compact foldable mats suit travellers. Wooden or stone tasbeeh beads are durable aids for dhikr.",
                        "Selecting Islamic Books & Educational Supplies" to "When ordering for families or madrasahs, verify language preference (Arabic, English, or bilingual) and age level so learning materials match the student's stage."
                    )

                    ResponsiveGrid(
                        items = guides,
                        columns = if (isDesktop || isTablet) 2 else 1,
                        horizontalGap = 16.dp,
                        verticalGap = 16.dp
                    ) { (title, body) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(title, style = MaterialTheme.typography.headlineMedium)
                                Text(
                                    text = body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { onOpenWhatsApp(WhatsAppMessages.ISLAMIC_CLOTHING) },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = PureWhite),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .widthIn(max = 360.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ask Our Team on WhatsApp", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        item {
            CorporateFooterCard(
                settings = settings,
                onNavigate = onNavigate,
                onOpenWhatsApp = { onOpenWhatsApp(WhatsAppMessages.ISLAMIC_PRODUCTS) },
                onOpenMaps = onOpenMaps
            )
        }
    }
}
