package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BusinessSettingsEntity
import com.example.data.local.CategoryEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SeedData
import com.example.data.model.BackendIntegrationStatus
import com.example.data.model.EnquiryStatus
import com.example.data.model.LogisticsServiceType
import com.example.data.model.QuotationFormInput
import com.example.data.model.SadcCountryConfig
import com.example.data.repository.LogisticsRepository
import com.example.data.repository.QuotationSubmitResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlHalalViewModel(
    private val repository: LogisticsRepository
) : ViewModel() {

    val businessSettings: StateFlow<BusinessSettingsEntity> = repository.observeBusinessSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultSettings())

    val sadcCountries: StateFlow<List<SadcCountryConfig>> = businessSettings
        .map { SeedData.parseSadc(it.sadcConfigSerialized) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultSadcCountries)

    val categories: StateFlow<List<CategoryEntity>> = repository.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultCategories())

    val visibleProducts: StateFlow<List<ProductEntity>> = repository.observeVisibleProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultProducts(0L))

    val allProducts: StateFlow<List<ProductEntity>> = repository.observeAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultProducts(0L))

    val allEnquiries: StateFlow<List<EnquiryEntity>> = repository.observeAllEnquiries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val adminSandboxEnabled: StateFlow<Boolean> = repository.adminSandboxModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private val _integrationStatus = MutableStateFlow(
        BackendIntegrationStatus(
            firebaseFirestoreConfigured = false,
            firebaseAuthConfigured = false,
            emailNotificationConfigured = false,
            configuredNotificationEmail = "",
            requiredActions = listOf("Checking configuration...")
        )
    )
    val integrationStatus: StateFlow<BackendIntegrationStatus> = _integrationStatus.asStateFlow()

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        visibleProducts,
        _productSearchQuery,
        _selectedCategoryId
    ) { products, query, categoryId ->
        val q = query.trim().lowercase()
        products.filter { product ->
            val matchesCat = categoryId == null || product.categoryId == categoryId
            val matchesQuery = q.isEmpty() ||
                product.name.lowercase().contains(q) ||
                product.description.lowercase().contains(q) ||
                product.variations.lowercase().contains(q)
            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SeedData.defaultProducts(0L))

    private val _quotationForm = MutableStateFlow(QuotationFormInput())
    val quotationForm: StateFlow<QuotationFormInput> = _quotationForm.asStateFlow()

    private val _quotationFieldErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val quotationFieldErrors: StateFlow<Map<String, String>> = _quotationFieldErrors.asStateFlow()

    private val _quotationSubmitResult = MutableStateFlow<QuotationSubmitResult?>(null)
    val quotationSubmitResult: StateFlow<QuotationSubmitResult?> = _quotationSubmitResult.asStateFlow()

    private val _isSubmittingQuotation = MutableStateFlow(false)
    val isSubmittingQuotation: StateFlow<Boolean> = _isSubmittingQuotation.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            _integrationStatus.value = repository.getIntegrationStatus()
        }
    }

    fun refreshIntegrationStatus() {
        viewModelScope.launch {
            _integrationStatus.value = repository.getIntegrationStatus()
        }
    }

    fun setProductSearchQuery(query: String) {
        _productSearchQuery.value = query
    }

    fun setSelectedCategory(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    fun updateQuotationForm(transform: (QuotationFormInput) -> QuotationFormInput) {
        _quotationForm.update(transform)
    }

    fun preselectServiceType(serviceType: LogisticsServiceType) {
        _quotationForm.update { it.copy(serviceType = serviceType) }
        _quotationSubmitResult.value = null
    }

    fun clearQuotationResult() {
        _quotationSubmitResult.value = null
    }

    fun resetQuotationForm() {
        _quotationForm.value = QuotationFormInput()
        _quotationFieldErrors.value = emptyMap()
        _quotationSubmitResult.value = null
    }

    fun submitQuotation() {
        if (_isSubmittingQuotation.value) return
        viewModelScope.launch {
            _isSubmittingQuotation.value = true
            val result = repository.submitQuotation(_quotationForm.value)
            when (result) {
                is QuotationSubmitResult.ValidationFailed -> {
                    _quotationFieldErrors.value = result.fieldErrors
                    _quotationSubmitResult.value = result
                }
                is QuotationSubmitResult.SavedLocallyCloudConfigRequired -> {
                    _quotationFieldErrors.value = emptyMap()
                    _quotationSubmitResult.value = result
                    _integrationStatus.value = result.integrationStatus
                }
                is QuotationSubmitResult.Error -> {
                    _quotationSubmitResult.value = result
                }
            }
            _isSubmittingQuotation.value = false
        }
    }

    fun setAdminSandboxEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAdminSandboxMode(enabled)
        }
    }

    fun updateEnquiryStatus(id: Long, status: EnquiryStatus) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(id, status)
        }
    }

    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.addOrUpdateProduct(product)
        }
    }

    fun toggleProductVisibility(product: ProductEntity) {
        viewModelScope.launch {
            repository.toggleProductVisibility(product)
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }

    fun saveCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun saveBusinessSettings(settings: BusinessSettingsEntity) {
        viewModelScope.launch {
            repository.updateBusinessSettings(settings)
            _integrationStatus.value = repository.getIntegrationStatus()
        }
    }

    fun toggleSadcCountry(code: String) {
        viewModelScope.launch {
            val updated = sadcCountries.value.map {
                if (it.code == code) it.copy(isServiceEnabled = !it.isServiceEnabled) else it
            }
            repository.updateSadcCountries(updated)
        }
    }

    class Factory(private val repository: LogisticsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlHalalViewModel(repository) as T
        }
    }
}
