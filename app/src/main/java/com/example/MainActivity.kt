package com.example

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.model.LogisticsServiceType
import com.example.data.model.ProductCategoryType
import com.example.data.repository.LogisticsRepository
import com.example.ui.AlHalalViewModel
import com.example.ui.components.AlHalalCorporateDesktopHeader
import com.example.ui.components.AlHalalTopBar
import com.example.ui.components.DesktopBreakpoint
import com.example.ui.components.FloatingWhatsAppFab
import com.example.ui.components.TabletBreakpoint
import com.example.ui.navigation.AppRoutes
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IslamicCategoryPageScreen
import com.example.ui.screens.IslamicGuidanceScreen
import com.example.ui.screens.IslamicSuppliesOverviewScreen
import com.example.ui.screens.LegalDocumentScreen
import com.example.ui.screens.LogisticsOverviewScreen
import com.example.ui.screens.LogisticsServiceDetailScreen
import com.example.ui.screens.LogisticsServicesCatalog
import com.example.ui.screens.QuoteScreen
import com.example.ui.theme.AlHalalTheme
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldRich
import com.example.ui.theme.PureWhite
import com.example.util.ExternalIntents
import com.example.util.WhatsAppMessages
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlHalalTheme {
                AlHalalApp()
            }
        }
        hideSystemBars()
        window.decorView.post { hideSystemBars() }
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                systemBarsBehavior =
                    android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(android.view.WindowInsets.Type.systemBars())
            }
        } else {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
fun AlHalalApp() {
    val context = LocalContext.current
    val repository = remember(context.applicationContext) {
        LogisticsRepository(context.applicationContext)
    }
    val viewModel: AlHalalViewModel = viewModel(
        factory = AlHalalViewModel.Factory(repository)
    )

    val settings by viewModel.businessSettings.collectAsStateWithLifecycle()
    val sadcCountries by viewModel.sadcCountries.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val visibleProducts by viewModel.visibleProducts.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val allEnquiries by viewModel.allEnquiries.collectAsStateWithLifecycle()
    val searchQuery by viewModel.productSearchQuery.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val quotationForm by viewModel.quotationForm.collectAsStateWithLifecycle()
    val fieldErrors by viewModel.quotationFieldErrors.collectAsStateWithLifecycle()
    val submitResult by viewModel.quotationSubmitResult.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmittingQuotation.collectAsStateWithLifecycle()
    val adminSandboxEnabled by viewModel.adminSandboxEnabled.collectAsStateWithLifecycle()
    val integrationStatus by viewModel.integrationStatus.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: AppRoutes.ROUTE_HOME

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigateTo(route: String) {
        if (currentRoute != route) {
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
        scope.launch { drawerState.close() }
    }

    fun navigateToQuote(service: LogisticsServiceType? = null) {
        if (service != null) {
            viewModel.preselectServiceType(service)
        }
        navigateTo(AppRoutes.ROUTE_QUOTE)
    }

    fun openWhatsApp(message: String) {
        ExternalIntents.openWhatsApp(context, settings.whatsappNumber, message)
    }

    fun openMaps() {
        ExternalIntents.openGoogleMapsForAddress(context, settings.address)
    }

    val contextualWhatsAppMsg = when (currentRoute) {
        AppRoutes.ROUTE_AIR_FREIGHT -> WhatsAppMessages.AIR_FREIGHT
        AppRoutes.ROUTE_SEA_FREIGHT -> WhatsAppMessages.SEA_FREIGHT
        AppRoutes.ROUTE_CUSTOMS_CLEARANCE -> WhatsAppMessages.CUSTOMS_CLEARANCE
        AppRoutes.ROUTE_DOOR_TO_DOOR -> WhatsAppMessages.DOOR_TO_DOOR
        AppRoutes.ROUTE_SADC_ROAD_FREIGHT -> WhatsAppMessages.ROAD_FREIGHT
        AppRoutes.ROUTE_MENS_CLOTHING,
        AppRoutes.ROUTE_WOMENS_CLOTHING -> WhatsAppMessages.ISLAMIC_CLOTHING
        AppRoutes.ROUTE_ISLAMIC_SUPPLIES,
        AppRoutes.ROUTE_ISLAMIC_ACCESSORIES,
        AppRoutes.ROUTE_ISLAMIC_GUIDANCE -> WhatsAppMessages.ISLAMIC_PRODUCTS
        else -> WhatsAppMessages.GENERAL_ENQUIRY
    }

    val topTitle = when (currentRoute) {
        AppRoutes.ROUTE_HOME -> "Home"
        AppRoutes.ROUTE_ABOUT -> "About Us"
        AppRoutes.ROUTE_LOGISTICS -> "Global Logistics"
        AppRoutes.ROUTE_AIR_FREIGHT -> "Air Freight"
        AppRoutes.ROUTE_SEA_FREIGHT -> "Sea Freight"
        AppRoutes.ROUTE_CUSTOMS_CLEARANCE -> "SA Customs Clearance"
        AppRoutes.ROUTE_DOOR_TO_DOOR -> "Door-to-Door Delivery"
        AppRoutes.ROUTE_SADC_ROAD_FREIGHT -> "SADC Road Freight"
        AppRoutes.ROUTE_ISLAMIC_SUPPLIES -> "Islamic Supplies"
        AppRoutes.ROUTE_MENS_CLOTHING -> "Men's Clothing"
        AppRoutes.ROUTE_WOMENS_CLOTHING -> "Women's Clothing"
        AppRoutes.ROUTE_ISLAMIC_ACCESSORIES -> "Accessories & Books"
        AppRoutes.ROUTE_ISLAMIC_GUIDANCE -> "Islamic Guidance"
        AppRoutes.ROUTE_QUOTE -> "Request a Quote"
        AppRoutes.ROUTE_CONTACT -> "Contact Us"
        AppRoutes.ROUTE_PRIVACY_POLICY -> "Privacy Policy"
        AppRoutes.ROUTE_TERMS -> "Terms & Conditions"
        AppRoutes.ROUTE_ADMIN -> "Admin Dashboard"
        else -> "Al-Halal Logistics"
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= TabletBreakpoint
        val isDesktop = maxWidth >= DesktopBreakpoint

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isWideScreen,
            drawerContent = {
                AppDrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = ::navigateTo,
                    onRequestQuote = { navigateToQuote(null) }
                )
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    if (isWideScreen) {
                        AlHalalCorporateDesktopHeader(
                            currentRoute = currentRoute,
                            whatsappDisplay = settings.whatsappNumber,
                            isDesktop = isDesktop,
                            onNavigate = ::navigateTo,
                            onRequestQuote = { navigateToQuote(null) },
                            onOpenWhatsApp = { openWhatsApp(contextualWhatsAppMsg) }
                        )
                    } else {
                        AlHalalTopBar(
                            title = topTitle,
                            onOpenMenu = { scope.launch { drawerState.open() } },
                            onRequestQuote = { navigateToQuote(null) },
                            onLogoClick = { navigateTo(AppRoutes.ROUTE_HOME) }
                        )
                    }
                },
                floatingActionButton = {
                    FloatingWhatsAppFab(
                        onClick = { openWhatsApp(contextualWhatsAppMsg) }
                    )
                },
                bottomBar = {
                    if (!isWideScreen) {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            NavigationBarItem(
                                selected = currentRoute == AppRoutes.ROUTE_HOME,
                                onClick = { navigateTo(AppRoutes.ROUTE_HOME) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") },
                                modifier = Modifier.testTag("bottom_nav_home")
                            )
                            NavigationBarItem(
                                selected = currentRoute == AppRoutes.ROUTE_LOGISTICS || currentRoute.startsWith("service_"),
                                onClick = { navigateTo(AppRoutes.ROUTE_LOGISTICS) },
                                icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Logistics") },
                                label = { Text("Logistics") },
                                modifier = Modifier.testTag("bottom_nav_logistics")
                            )
                            NavigationBarItem(
                                selected = currentRoute.startsWith("islamic_"),
                                onClick = { navigateTo(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) },
                                icon = { Icon(Icons.Default.Storefront, contentDescription = "Supplies") },
                                label = { Text("Supplies") },
                                modifier = Modifier.testTag("bottom_nav_islamic")
                            )
                            NavigationBarItem(
                                selected = currentRoute == AppRoutes.ROUTE_QUOTE,
                                onClick = { navigateToQuote(null) },
                                icon = { Icon(Icons.Default.RequestQuote, contentDescription = "Quote") },
                                label = { Text("Quote") },
                                modifier = Modifier.testTag("bottom_nav_quote")
                            )
                            NavigationBarItem(
                                selected = currentRoute == AppRoutes.ROUTE_CONTACT,
                                onClick = { navigateTo(AppRoutes.ROUTE_CONTACT) },
                                icon = { Icon(Icons.Default.ContactPhone, contentDescription = "Contact") },
                                label = { Text("Contact") },
                                modifier = Modifier.testTag("bottom_nav_contact")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.ROUTE_HOME,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(AppRoutes.ROUTE_HOME) {
                            HomeScreen(
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onRequestQuote = ::navigateToQuote,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_ABOUT) {
                            AboutUsScreen(
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_LOGISTICS) {
                            LogisticsOverviewScreen(
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onRequestQuote = ::navigateToQuote,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        LogisticsServicesCatalog.forEach { svc ->
                            composable(svc.route) {
                                LogisticsServiceDetailScreen(
                                    service = svc,
                                    sadcCountries = sadcCountries,
                                    settings = settings,
                                    onNavigate = ::navigateTo,
                                    onRequestQuote = ::navigateToQuote,
                                    onOpenWhatsApp = ::openWhatsApp,
                                    onOpenMaps = ::openMaps
                                )
                            }
                        }
                        composable(AppRoutes.ROUTE_ISLAMIC_SUPPLIES) {
                            IslamicSuppliesOverviewScreen(
                                products = filteredProducts,
                                categories = categories,
                                searchQuery = searchQuery,
                                selectedCategoryId = selectedCategoryId,
                                settings = settings,
                                onSearchQueryChange = viewModel::setProductSearchQuery,
                                onSelectCategory = viewModel::setSelectedCategory,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_MENS_CLOTHING) {
                            IslamicCategoryPageScreen(
                                categoryType = ProductCategoryType.MENS_CLOTHING,
                                products = visibleProducts,
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_WOMENS_CLOTHING) {
                            IslamicCategoryPageScreen(
                                categoryType = ProductCategoryType.WOMENS_CLOTHING,
                                products = visibleProducts,
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES) {
                            IslamicCategoryPageScreen(
                                categoryType = ProductCategoryType.ISLAMIC_ACCESSORIES,
                                products = visibleProducts,
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_ISLAMIC_GUIDANCE) {
                            IslamicGuidanceScreen(
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_QUOTE) {
                            QuoteScreen(
                                form = quotationForm,
                                fieldErrors = fieldErrors,
                                submitResult = submitResult,
                                isSubmitting = isSubmitting,
                                settings = settings,
                                onUpdateForm = viewModel::updateQuotationForm,
                                onSubmit = viewModel::submitQuotation,
                                onResetForm = viewModel::resetQuotationForm,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_CONTACT) {
                            ContactUsScreen(
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onRequestQuote = { navigateToQuote(null) },
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_PRIVACY_POLICY) {
                            LegalDocumentScreen(
                                isPrivacyPolicy = true,
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_TERMS) {
                            LegalDocumentScreen(
                                isPrivacyPolicy = false,
                                settings = settings,
                                onNavigate = ::navigateTo,
                                onOpenWhatsApp = ::openWhatsApp,
                                onOpenMaps = ::openMaps
                            )
                        }
                        composable(AppRoutes.ROUTE_ADMIN) {
                            AdminDashboardScreen(
                                isSandboxEnabled = adminSandboxEnabled,
                                integrationStatus = integrationStatus,
                                enquiries = allEnquiries,
                                products = allProducts,
                                categories = categories,
                                settings = settings,
                                sadcCountries = sadcCountries,
                                onSetSandboxEnabled = viewModel::setAdminSandboxEnabled,
                                onUpdateEnquiryStatus = viewModel::updateEnquiryStatus,
                                onSaveProduct = viewModel::saveProduct,
                                onToggleProductVisibility = viewModel::toggleProductVisibility,
                                onDeleteProduct = viewModel::deleteProduct,
                                onSaveCategory = viewModel::saveCategory,
                                onSaveBusinessSettings = viewModel::saveBusinessSettings,
                                onToggleSadcCountry = viewModel::toggleSadcCountry
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onRequestQuote: () -> Unit
) {
    var logisticsExpanded by remember { mutableStateOf(true) }
    var islamicExpanded by remember { mutableStateOf(true) }

    ModalDrawerSheet(
        modifier = Modifier.width(304.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldDeep)
                    .padding(20.dp)
            ) {
                Text(
                    text = "AL-HALAL LOGISTICS N TRANS",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldBright,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Springs, Gauteng, South Africa",
                    style = MaterialTheme.typography.bodySmall,
                    color = PureWhite.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            NavigationDrawerItem(
                label = { Text("Home") },
                selected = currentRoute == AppRoutes.ROUTE_HOME,
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_HOME) }
            )
            NavigationDrawerItem(
                label = { Text("About Us") },
                selected = currentRoute == AppRoutes.ROUTE_ABOUT,
                icon = { Icon(Icons.Default.Business, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_ABOUT) },
                modifier = Modifier.testTag("drawer_about")
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            // Expandable Dropdown: Logistics Services
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { logisticsExpanded = !logisticsExpanded }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = EmeraldDeep)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Logistics Services", style = MaterialTheme.typography.titleSmall, color = GoldRich)
                }
                Icon(
                    imageVector = if (logisticsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Logistics dropdown"
                )
            }
            AnimatedVisibility(visible = logisticsExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    DrawerSubItem("All Logistics Services", currentRoute == AppRoutes.ROUTE_LOGISTICS) {
                        onNavigate(AppRoutes.ROUTE_LOGISTICS)
                    }
                    DrawerSubItem("International Air Freight", currentRoute == AppRoutes.ROUTE_AIR_FREIGHT) {
                        onNavigate(AppRoutes.ROUTE_AIR_FREIGHT)
                    }
                    DrawerSubItem("International Sea Freight", currentRoute == AppRoutes.ROUTE_SEA_FREIGHT) {
                        onNavigate(AppRoutes.ROUTE_SEA_FREIGHT)
                    }
                    DrawerSubItem("SA Customs Clearance", currentRoute == AppRoutes.ROUTE_CUSTOMS_CLEARANCE) {
                        onNavigate(AppRoutes.ROUTE_CUSTOMS_CLEARANCE)
                    }
                    DrawerSubItem("Global Door-to-Door", currentRoute == AppRoutes.ROUTE_DOOR_TO_DOOR) {
                        onNavigate(AppRoutes.ROUTE_DOOR_TO_DOOR)
                    }
                    DrawerSubItem("SADC Road Freight", currentRoute == AppRoutes.ROUTE_SADC_ROAD_FREIGHT) {
                        onNavigate(AppRoutes.ROUTE_SADC_ROAD_FREIGHT)
                    }
                }
            }

            // Expandable Dropdown: Islamic Supplies
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { islamicExpanded = !islamicExpanded }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = EmeraldDeep)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Islamic Supplies", style = MaterialTheme.typography.titleSmall, color = GoldRich)
                }
                Icon(
                    imageVector = if (islamicExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Islamic Supplies dropdown"
                )
            }
            AnimatedVisibility(visible = islamicExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    DrawerSubItem("Catalogue Overview", currentRoute == AppRoutes.ROUTE_ISLAMIC_SUPPLIES) {
                        onNavigate(AppRoutes.ROUTE_ISLAMIC_SUPPLIES)
                    }
                    DrawerSubItem("Men's Islamic Clothing", currentRoute == AppRoutes.ROUTE_MENS_CLOTHING) {
                        onNavigate(AppRoutes.ROUTE_MENS_CLOTHING)
                    }
                    DrawerSubItem("Women's Islamic Clothing", currentRoute == AppRoutes.ROUTE_WOMENS_CLOTHING) {
                        onNavigate(AppRoutes.ROUTE_WOMENS_CLOTHING)
                    }
                    DrawerSubItem("Accessories & Books", currentRoute == AppRoutes.ROUTE_ISLAMIC_ACCESSORIES) {
                        onNavigate(AppRoutes.ROUTE_ISLAMIC_ACCESSORIES)
                    }
                    DrawerSubItem("Islamic Product Guidance", currentRoute == AppRoutes.ROUTE_ISLAMIC_GUIDANCE) {
                        onNavigate(AppRoutes.ROUTE_ISLAMIC_GUIDANCE)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            NavigationDrawerItem(
                label = { Text("Request a Quote", fontWeight = FontWeight.Bold) },
                selected = currentRoute == AppRoutes.ROUTE_QUOTE,
                icon = { Icon(Icons.Default.RequestQuote, contentDescription = null) },
                onClick = onRequestQuote
            )
            NavigationDrawerItem(
                label = { Text("Contact Us") },
                selected = currentRoute == AppRoutes.ROUTE_CONTACT,
                icon = { Icon(Icons.Default.ContactPhone, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_CONTACT) }
            )
            NavigationDrawerItem(
                label = { Text("Privacy Policy (POPIA)") },
                selected = currentRoute == AppRoutes.ROUTE_PRIVACY_POLICY,
                icon = { Icon(Icons.Default.Policy, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_PRIVACY_POLICY) }
            )
            NavigationDrawerItem(
                label = { Text("Terms & Conditions") },
                selected = currentRoute == AppRoutes.ROUTE_TERMS,
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_TERMS) }
            )
            NavigationDrawerItem(
                label = { Text("Admin Dashboard") },
                selected = currentRoute == AppRoutes.ROUTE_ADMIN,
                icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                onClick = { onNavigate(AppRoutes.ROUTE_ADMIN) },
                modifier = Modifier.testTag("drawer_admin")
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DrawerSubItem(label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
        selected = selected,
        onClick = onClick
    )
}
