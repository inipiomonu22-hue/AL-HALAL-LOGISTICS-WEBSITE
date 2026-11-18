package com.example.data.model

enum class LogisticsServiceType(
    val title: String,
    val shortTitle: String,
    val routeKey: String
) {
    AIR_FREIGHT("International Air Freight", "Air Freight", "air_freight"),
    SEA_FREIGHT("International Sea Freight", "Sea Freight", "sea_freight"),
    CUSTOMS_CLEARANCE("South African Customs Clearance", "Customs Clearance", "customs_clearance"),
    DOOR_TO_DOOR("Global Door-to-Door Delivery", "Door-to-Door", "door_to_door"),
    SADC_ROAD_FREIGHT("SADC Road Freight", "SADC Road Freight", "sadc_road_freight"),
    GENERAL_ENQUIRY("General Logistics Enquiry", "General Enquiry", "general_enquiry");

    companion object {
        fun fromNameOrDefault(raw: String): LogisticsServiceType =
            entries.firstOrNull { it.name == raw || it.routeKey == raw || it.title == raw }
                ?: GENERAL_ENQUIRY
    }
}

enum class SeaFreightOption(val label: String) {
    FCL("Full Container Load (FCL)"),
    LCL("Less than Container Load (LCL)"),
    NOT_SURE("Not sure — advise me");

    companion object {
        fun fromNameOrDefault(raw: String): SeaFreightOption =
            entries.firstOrNull { it.name == raw || it.label == raw } ?: NOT_SURE
    }
}

enum class EnquiryStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    QUOTED("Quoted"),
    COMPLETED("Completed");

    companion object {
        fun fromNameOrDefault(raw: String): EnquiryStatus =
            entries.firstOrNull { it.name == raw || it.label == raw } ?: PENDING
    }
}

enum class ProductCategoryType(
    val id: String,
    val displayName: String,
    val subtitle: String
) {
    MENS_CLOTHING(
        id = "mens_clothing",
        displayName = "Men's Islamic Clothing",
        subtitle = "Thobes, Jubbas, Kufi & Prayer Caps, Traditional Garments"
    ),
    WOMENS_CLOTHING(
        id = "womens_clothing",
        displayName = "Women's Islamic Clothing",
        subtitle = "Abayas, Hijabs, Khimars, Jilbabs & Modest Dresses"
    ),
    ISLAMIC_ACCESSORIES(
        id = "islamic_accessories",
        displayName = "Islamic Accessories & Products",
        subtitle = "Prayer Mats, Tasbeeh, Islamic Decor & Lifestyle Supplies"
    ),
    BOOKS_AND_EDUCATION(
        id = "books_education",
        displayName = "Islamic Books & Educational Materials",
        subtitle = "Learning Resources, Study Guides & Educational Supplies"
    );

    companion object {
        fun fromIdOrDefault(raw: String): ProductCategoryType =
            entries.firstOrNull { it.id == raw || it.name == raw } ?: ISLAMIC_ACCESSORIES
    }
}

data class SadcCountryConfig(
    val code: String,
    val name: String,
    val corridorNote: String,
    val isServiceEnabled: Boolean
)

data class BackendIntegrationStatus(
    val firebaseFirestoreConfigured: Boolean,
    val firebaseAuthConfigured: Boolean,
    val emailNotificationConfigured: Boolean,
    val configuredNotificationEmail: String,
    val requiredActions: List<String>
)

data class QuotationFormInput(
    val fullName: String = "",
    val companyName: String = "",
    val whatsappNumber: String = "",
    val email: String = "",
    val country: String = "",
    val originCountry: String = "",
    val collectionCity: String = "",
    val destinationCountry: String = "",
    val deliveryCity: String = "",
    val collectionAddress: String = "",
    val deliveryAddress: String = "",
    val goodsType: String = "",
    val cargoDescription: String = "",
    val estimatedWeight: String = "",
    val cargoDimensions: String = "",
    val numberOfPackages: String = "",
    val estimatedValue: String = "",
    val serviceType: LogisticsServiceType = LogisticsServiceType.AIR_FREIGHT,
    val seaFreightOption: SeaFreightOption = SeaFreightOption.NOT_SURE,
    val roadVehicleRequirements: String = "",
    val specialInstructions: String = "",
    val attachmentDisplayName: String = "",
    val attachmentUriString: String = "",
    val privacyConsentAccepted: Boolean = false
)
