package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.local.AlHalalDao
import com.example.data.local.AlHalalDatabase
import com.example.data.local.BusinessSettingsEntity
import com.example.data.local.CategoryEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SeedData
import com.example.data.model.BackendIntegrationStatus
import com.example.data.model.EnquiryStatus
import com.example.data.model.QuotationFormInput
import com.example.data.model.SadcCountryConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "al_halal_prefs")

sealed interface QuotationSubmitResult {
    data class ValidationFailed(val fieldErrors: Map<String, String>) : QuotationSubmitResult
    data class SavedLocallyCloudConfigRequired(
        val enquiry: EnquiryEntity,
        val integrationStatus: BackendIntegrationStatus
    ) : QuotationSubmitResult
    data class Error(val message: String) : QuotationSubmitResult
}

class LogisticsRepository(
    private val context: Context,
    private val dao: AlHalalDao = AlHalalDatabase.getInstance(context).alHalalDao()
) {
    private val sandboxKey = booleanPreferencesKey("admin_sandbox_mode_enabled")

    val adminSandboxModeFlow: Flow<Boolean> = context.appDataStore.data.map { prefs ->
        prefs[sandboxKey] ?: false
    }

    suspend fun setAdminSandboxMode(enabled: Boolean) {
        context.appDataStore.edit { prefs ->
            prefs[sandboxKey] = enabled
        }
    }

    suspend fun ensureSeeded() {
        if (dao.countCategories() == 0) {
            dao.insertCategories(SeedData.defaultCategories())
        }
        if (dao.countProducts() == 0) {
            dao.insertProducts(SeedData.defaultProducts(System.currentTimeMillis()))
        }
        if (dao.getBusinessSettingsOnce() == null) {
            dao.upsertBusinessSettings(SeedData.defaultSettings())
        }
    }

    fun observeBusinessSettings(): Flow<BusinessSettingsEntity> =
        dao.observeBusinessSettings().map { it ?: SeedData.defaultSettings() }

    fun observeCategories(): Flow<List<CategoryEntity>> = dao.observeCategories()
    fun observeVisibleProducts(): Flow<List<ProductEntity>> = dao.observeVisibleProducts()
    fun observeAllProducts(): Flow<List<ProductEntity>> = dao.observeAllProducts()
    fun observeAllEnquiries(): Flow<List<EnquiryEntity>> = dao.observeAllEnquiries()

    suspend fun getIntegrationStatus(): BackendIntegrationStatus {
        val settings = dao.getBusinessSettingsOnce() ?: SeedData.defaultSettings()
        val hasGoogleServices = try {
            context.resources.getIdentifier("default_web_client_id", "string", context.packageName) != 0 &&
                context.resources.getIdentifier("firestore_database_id", "string", context.packageName) != 0
        } catch (_: Exception) {
            false
        }
        val hasEmailConfigured = settings.notificationEmailPlaceholder.isNotBlank()
        val missing = buildList {
            if (!hasGoogleServices) {
                add("Firebase Firestore & Google Sign-In Auth not provisioned (missing google-services.json & firebase_applet_config.xml).")
            }
            if (!hasEmailConfigured) {
                add("Notification email address & server-side email webhook/SMTP provider not configured.")
            } else {
                add("Server-side email dispatch endpoint (Cloud Function / SMTP API) requires backend deployment.")
            }
        }
        return BackendIntegrationStatus(
            firebaseFirestoreConfigured = hasGoogleServices,
            firebaseAuthConfigured = hasGoogleServices,
            emailNotificationConfigured = false, // Honest: no live SMTP/Cloud Function endpoint configured
            configuredNotificationEmail = settings.notificationEmailPlaceholder,
            requiredActions = missing
        )
    }

    fun validateQuotation(form: QuotationFormInput): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (form.fullName.trim().length < 2) errors["fullName"] = "Full name is required."
        if (form.whatsappNumber.trim().length < 6) errors["whatsappNumber"] = "Valid WhatsApp number is required."
        val emailTrim = form.email.trim()
        if (emailTrim.isEmpty() || !emailTrim.contains("@") || !emailTrim.contains(".")) {
            errors["email"] = "Valid email address is required."
        }
        if (form.country.trim().isEmpty()) errors["country"] = "Country is required."
        if (form.originCountry.trim().isEmpty()) errors["originCountry"] = "Origin country is required."
        if (form.collectionCity.trim().isEmpty()) errors["collectionCity"] = "Collection city is required."
        if (form.destinationCountry.trim().isEmpty()) errors["destinationCountry"] = "Destination country is required."
        if (form.deliveryCity.trim().isEmpty()) errors["deliveryCity"] = "Delivery city is required."
        if (form.collectionAddress.trim().isEmpty()) errors["collectionAddress"] = "Collection address is required."
        if (form.deliveryAddress.trim().isEmpty()) errors["deliveryAddress"] = "Delivery address is required."
        if (form.goodsType.trim().isEmpty()) errors["goodsType"] = "Type of goods is required."
        if (form.cargoDescription.trim().length < 5) errors["cargoDescription"] = "Please describe the cargo."
        if (form.estimatedWeight.trim().isEmpty()) errors["estimatedWeight"] = "Estimated weight is required."
        if (form.cargoDimensions.trim().isEmpty()) errors["cargoDimensions"] = "Cargo dimensions are required."
        if (form.numberOfPackages.trim().isEmpty()) errors["numberOfPackages"] = "Number of packages is required."
        if (!form.privacyConsentAccepted) {
            errors["privacyConsent"] = "Consent to the Privacy Policy is required."
        }
        return errors
    }

    suspend fun submitQuotation(form: QuotationFormInput): QuotationSubmitResult {
        val errors = validateQuotation(form)
        if (errors.isNotEmpty()) {
            return QuotationSubmitResult.ValidationFailed(errors)
        }
        return try {
            val now = System.currentTimeMillis()
            val dateCode = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date(now))
            val isoTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date(now))
            val suffix = Random.nextInt(1000, 9999)
            val reference = "AHL-$dateCode-$suffix"

            val integrationStatus = getIntegrationStatus()

            val entity = EnquiryEntity(
                referenceNumber = reference,
                fullName = form.fullName.trim(),
                companyName = form.companyName.trim(),
                whatsappNumber = form.whatsappNumber.trim(),
                email = form.email.trim(),
                country = form.country.trim(),
                originCountry = form.originCountry.trim(),
                collectionCity = form.collectionCity.trim(),
                destinationCountry = form.destinationCountry.trim(),
                deliveryCity = form.deliveryCity.trim(),
                collectionAddress = form.collectionAddress.trim(),
                deliveryAddress = form.deliveryAddress.trim(),
                goodsType = form.goodsType.trim(),
                cargoDescription = form.cargoDescription.trim(),
                estimatedWeight = form.estimatedWeight.trim(),
                cargoDimensions = form.cargoDimensions.trim(),
                numberOfPackages = form.numberOfPackages.trim(),
                estimatedValue = form.estimatedValue.trim(),
                serviceType = form.serviceType.title,
                seaFreightOption = if (form.serviceType.routeKey == "sea_freight") form.seaFreightOption.label else "",
                roadVehicleRequirements = if (form.serviceType.routeKey == "sadc_road_freight") form.roadVehicleRequirements.trim() else "",
                specialInstructions = form.specialInstructions.trim(),
                attachmentDisplayName = form.attachmentDisplayName.trim(),
                attachmentUriString = form.attachmentUriString.trim(),
                status = EnquiryStatus.PENDING.label,
                cloudSyncState = if (integrationStatus.firebaseFirestoreConfigured) {
                    "PENDING_SYNC"
                } else {
                    "LOCAL_DRAFT_CLOUD_UNCONFIGURED"
                },
                createdAtEpochMs = now,
                createdAtIso = isoTimestamp
            )
            val insertedId = dao.insertEnquiry(entity)
            QuotationSubmitResult.SavedLocallyCloudConfigRequired(
                enquiry = entity.copy(id = insertedId),
                integrationStatus = integrationStatus
            )
        } catch (e: Exception) {
            QuotationSubmitResult.Error(e.localizedMessage ?: "Failed to process quotation request.")
        }
    }

    suspend fun updateEnquiryStatus(id: Long, status: EnquiryStatus) {
        dao.updateEnquiryStatus(id, status.label)
    }

    suspend fun addOrUpdateProduct(product: ProductEntity) {
        if (product.id == 0L) {
            dao.insertProduct(product.copy(createdAtEpochMs = System.currentTimeMillis()))
        } else {
            dao.updateProduct(product)
        }
    }

    suspend fun toggleProductVisibility(product: ProductEntity) {
        dao.updateProduct(product.copy(isVisible = !product.isVisible))
    }

    suspend fun deleteProduct(id: Long) {
        dao.deleteProductById(id)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        dao.upsertCategory(category)
    }

    suspend fun updateBusinessSettings(settings: BusinessSettingsEntity) {
        dao.upsertBusinessSettings(settings)
    }

    suspend fun updateSadcCountries(countries: List<SadcCountryConfig>) {
        val current = dao.getBusinessSettingsOnce() ?: SeedData.defaultSettings()
        dao.upsertBusinessSettings(
            current.copy(sadcConfigSerialized = SeedData.serializeSadc(countries))
        )
    }
}
