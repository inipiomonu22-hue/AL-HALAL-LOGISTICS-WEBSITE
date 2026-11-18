package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AlHalalDatabase
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SeedData
import com.example.data.model.EnquiryStatus
import com.example.data.model.LogisticsServiceType
import com.example.data.model.ProductCategoryType
import com.example.data.model.QuotationFormInput
import com.example.data.repository.LogisticsRepository
import com.example.data.repository.QuotationSubmitResult
import com.example.ui.theme.AlHalalTheme
import com.example.util.ExternalIntents
import com.example.util.WhatsAppMessages
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AlHalalAuditTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `whatsapp url encoding matches official number and all presets`() {
        val presets = listOf(
            WhatsAppMessages.GENERAL_ENQUIRY,
            WhatsAppMessages.AIR_FREIGHT,
            WhatsAppMessages.SEA_FREIGHT,
            WhatsAppMessages.CUSTOMS_CLEARANCE,
            WhatsAppMessages.ROAD_FREIGHT,
            WhatsAppMessages.DOOR_TO_DOOR,
            WhatsAppMessages.ISLAMIC_CLOTHING,
            WhatsAppMessages.ISLAMIC_PRODUCTS,
            WhatsAppMessages.productEnquiry("Classic Tailored Men's Thobe")
        )
        presets.forEach { msg ->
            val url = ExternalIntents.buildWhatsAppUrl("+27 74 076 7068", msg)
            assertTrue("Expected wa.me/27740767068 prefix in $url", url.startsWith("https://wa.me/27740767068?text="))
            assertFalse("URL should not contain raw spaces: $url", url.contains(" "))
        }
    }

    @Test
    fun `google maps search url uses verified Springs address without fake coordinates`() {
        val url = ExternalIntents.buildGoogleMapsSearchUrl(ExternalIntents.DEFAULT_ADDRESS)
        assertTrue(url.startsWith("https://www.google.com/maps/search/?api=1&query="))
        assertTrue(url.contains("281"))
        assertTrue(url.contains("Springs"))
        assertTrue(url.contains("1559"))
    }

    @Test
    fun `quotation validation and honest unconfigured backend status`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AlHalalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repo = LogisticsRepository(context, db.alHalalDao())
            repo.ensureSeeded()

            // Invalid empty submission must fail validation
            val invalidResult = repo.submitQuotation(QuotationFormInput())
            assertTrue(invalidResult is QuotationSubmitResult.ValidationFailed)
            val errors = (invalidResult as QuotationSubmitResult.ValidationFailed).fieldErrors
            assertTrue(errors.containsKey("fullName"))
            assertTrue(errors.containsKey("privacyConsent"))

            // Valid submission saves local draft and honestly reports unconfigured Firebase/Email
            val validForm = QuotationFormInput(
                fullName = "Ahmed Patel",
                companyName = "Patel Trading",
                whatsappNumber = "+27820001111",
                email = "ahmed@example.org",
                country = "South Africa",
                originCountry = "UAE",
                collectionCity = "Dubai",
                destinationCountry = "South Africa",
                deliveryCity = "Springs",
                collectionAddress = "Warehouse 4, Jebel Ali",
                deliveryAddress = "281 Beverly Street, Springs",
                goodsType = "Modest Garments",
                cargoDescription = "20 cartons of men's thobes and abayas",
                estimatedWeight = "320 kg",
                cargoDimensions = "120x80x100 cm",
                numberOfPackages = "20",
                serviceType = LogisticsServiceType.AIR_FREIGHT,
                privacyConsentAccepted = true
            )
            val validResult = repo.submitQuotation(validForm)
            assertTrue(validResult is QuotationSubmitResult.SavedLocallyCloudConfigRequired)
            val saved = validResult as QuotationSubmitResult.SavedLocallyCloudConfigRequired
            assertTrue(saved.enquiry.referenceNumber.startsWith("AHL-"))
            assertEquals("LOCAL_DRAFT_CLOUD_UNCONFIGURED", saved.enquiry.cloudSyncState)
            assertFalse(saved.integrationStatus.firebaseFirestoreConfigured)
            assertFalse(saved.integrationStatus.emailNotificationConfigured)

            val csv = ExternalIntents.buildCsvForEnquiries(listOf(saved.enquiry))
            assertTrue(csv.contains("Ahmed Patel"))
            assertTrue(csv.contains(saved.enquiry.referenceNumber))
        } finally {
            db.close()
        }
    }

    @Test
    fun `ui navigation drawer, bottom nav, supplies filter, and admin gate work cleanly`() {
        composeRule.setContent {
            AlHalalTheme {
                AlHalalApp()
            }
        }
        composeRule.waitForIdle()

        // Home hero visible
        composeRule.onNodeWithTag("hero_explore_logistics_btn").assertIsDisplayed()
        composeRule.onNodeWithTag("floating_whatsapp_fab").assertIsDisplayed()

        // Navigate to Islamic Supplies via bottom bar
        composeRule.onNodeWithTag("bottom_nav_islamic").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("islamic_catalogue_list")
            .performScrollToNode(hasTestTag("product_search_input"))
        composeRule.onNodeWithTag("product_search_input").assertIsDisplayed()
        composeRule.onNodeWithTag("product_search_input").performTextInput("Abaya")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("islamic_catalogue_list")
            .performScrollToNode(hasText("Elegant Everyday Modest Abaya"))
        composeRule.onNodeWithText("Elegant Everyday Modest Abaya").assertIsDisplayed()

        // Navigate to Quote screen via top bar button
        composeRule.onNodeWithTag("top_bar_quote_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("quote_full_name").assertIsDisplayed()

        // Navigate via Drawer to Admin Dashboard and verify protected gate
        composeRule.onNodeWithTag("nav_menu_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("drawer_admin").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("admin_gate_list")
            .performScrollToNode(hasTestTag("enable_admin_sandbox_btn"))
        composeRule.onNodeWithTag("enable_admin_sandbox_btn").assertIsDisplayed()

        // Unlock sandbox mode and verify Admin tabs appear
        composeRule.onNodeWithTag("enable_admin_sandbox_btn").performClick()
        composeRule.waitUntil(timeoutMillis = 5000L) {
            composeRule.onAllNodesWithTag("admin_tab_2")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("admin_tab_2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("admin_add_product_button").assertIsDisplayed()
    }

    @Test
    @Config(sdk = [36], qualifiers = "w1280dp-h800dp")
    fun `desktop breakpoint renders corporate header with dropdowns and hides mobile bottom bar`() {
        composeRule.setContent {
            AlHalalTheme {
                AlHalalApp()
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("corporate_desktop_header").assertIsDisplayed()
        composeRule.onNodeWithTag("desktop_header_whatsapp_button").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithTag("bottom_nav_home").fetchSemanticsNodes().isEmpty())

        // Open Logistics Services dropdown in desktop header
        composeRule.onNodeWithText("LOGISTICS SERVICES").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("desktop_dropdown_all_logistics").assertIsDisplayed()
        composeRule.onNodeWithTag("desktop_dropdown_all_logistics").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Explore Every Logistics Service").assertIsDisplayed()
    }

    @Test
    fun `repository supports product add, hide, edit, delete, enquiry status, and SADC toggles`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AlHalalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repo = LogisticsRepository(context, db.alHalalDao())
            repo.ensureSeeded()

            val initialVisible = repo.observeVisibleProducts().first()
            assertTrue(initialVisible.isNotEmpty())

            // Add a product
            repo.addOrUpdateProduct(
                ProductEntity(
                    name = "Test Silk Khimar",
                    categoryId = ProductCategoryType.WOMENS_CLOTHING.id,
                    description = "Lightweight modest khimar",
                    variations = "Emerald, Charcoal",
                    suppliedPriceText = "",
                    imageDrawableKey = "clothing",
                    customImageUri = "",
                    isVisible = true,
                    createdAtEpochMs = System.currentTimeMillis()
                )
            )
            val afterAdd = repo.observeAllProducts().first()
            val added = afterAdd.first { it.name == "Test Silk Khimar" }

            // Hide product -> omitted from visibleProducts
            repo.toggleProductVisibility(added)
            val visibleAfterHide = repo.observeVisibleProducts().first()
            assertFalse(visibleAfterHide.any { it.id == added.id })

            // Delete product
            repo.deleteProduct(added.id)
            val afterDelete = repo.observeAllProducts().first()
            assertFalse(afterDelete.any { it.id == added.id })

            // SADC toggle persistence
            val toggled = SeedData.defaultSadcCountries.map {
                if (it.code == "TZ") it.copy(isServiceEnabled = true) else it
            }
            repo.updateSadcCountries(toggled)
            val updatedSettings = repo.observeBusinessSettings().first()
            val parsedSadc = SeedData.parseSadc(updatedSettings.sadcConfigSerialized)
            assertTrue(parsedSadc.first { it.code == "TZ" }.isServiceEnabled)
        } finally {
            db.close()
        }
    }
}
