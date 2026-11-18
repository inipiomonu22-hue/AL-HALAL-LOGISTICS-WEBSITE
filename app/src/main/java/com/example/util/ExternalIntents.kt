package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.local.EnquiryEntity

object WhatsAppMessages {
    const val GENERAL_ENQUIRY =
        "Hello AL-HALAL LOGISTICS N TRANS. I would like to make a general business enquiry."
    const val AIR_FREIGHT =
        "Hello, I would like to request an international air freight quotation."
    const val SEA_FREIGHT =
        "Hello, I would like to request a sea freight quotation."
    const val CUSTOMS_CLEARANCE =
        "Hello, I need assistance with customs clearance in South Africa."
    const val ROAD_FREIGHT =
        "Hello, I would like information about SADC regional road freight."
    const val DOOR_TO_DOOR =
        "Hello, I would like to arrange international door-to-door cargo delivery."
    const val ISLAMIC_CLOTHING =
        "Hello, I would like to enquire about Islamic clothing."
    const val ISLAMIC_PRODUCTS =
        "Hello, I would like to enquire about your general Islamic products and supplies."

    fun productEnquiry(productName: String): String =
        "Hello AL-HALAL LOGISTICS N TRANS. I would like to enquire about $productName."
}

object ExternalIntents {
    const val DEFAULT_WHATSAPP_DIGITS = "27740767068"
    const val DEFAULT_ADDRESS =
        "281 Beverly Street, New State Areas, Springs, Gauteng, 1559, South Africa"

    fun normalizePhoneDigits(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        return digits.ifBlank { DEFAULT_WHATSAPP_DIGITS }
    }

    fun buildWhatsAppUrl(phoneNumber: String, message: String): String {
        val digits = normalizePhoneDigits(phoneNumber)
        return "https://wa.me/$digits?text=${Uri.encode(message)}"
    }

    fun openWhatsApp(
        context: Context,
        phoneNumber: String = DEFAULT_WHATSAPP_DIGITS,
        message: String = WhatsAppMessages.GENERAL_ENQUIRY
    ) {
        val uri = Uri.parse(buildWhatsAppUrl(phoneNumber, message))
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(
                context,
                "Unable to open WhatsApp or browser for $uri",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun buildGoogleMapsSearchUrl(address: String): String =
        "https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"

    fun openGoogleMapsForAddress(context: Context, address: String = DEFAULT_ADDRESS) {
        val uri = Uri.parse(buildGoogleMapsSearchUrl(address))
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(
                context,
                "No map or browser application found to open address.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun formatQuotationForWhatsApp(enquiry: EnquiryEntity): String = buildString {
        appendLine("Hello AL-HALAL LOGISTICS N TRANS.")
        appendLine("Quotation Request Ref: ${enquiry.referenceNumber}")
        appendLine("Customer: ${enquiry.fullName}")
        if (enquiry.companyName.isNotBlank()) appendLine("Company: ${enquiry.companyName}")
        appendLine("WhatsApp: ${enquiry.whatsappNumber}")
        appendLine("Email: ${enquiry.email}")
        appendLine("Service: ${enquiry.serviceType}")
        if (enquiry.seaFreightOption.isNotBlank()) appendLine("Sea Option: ${enquiry.seaFreightOption}")
        if (enquiry.roadVehicleRequirements.isNotBlank()) appendLine("Road Req: ${enquiry.roadVehicleRequirements}")
        appendLine("Route: ${enquiry.collectionCity}, ${enquiry.originCountry} -> ${enquiry.deliveryCity}, ${enquiry.destinationCountry}")
        appendLine("Goods: ${enquiry.goodsType} | Packages: ${enquiry.numberOfPackages} | Weight: ${enquiry.estimatedWeight} | Dim: ${enquiry.cargoDimensions}")
        appendLine("Description: ${enquiry.cargoDescription}")
        if (enquiry.specialInstructions.isNotBlank()) appendLine("Notes: ${enquiry.specialInstructions}")
    }.trim()

    fun shareCsvText(context: Context, csvContent: String, subject: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, csvContent)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(Intent.createChooser(sendIntent, "Export Enquiries CSV"))
        } catch (_: Exception) {
            Toast.makeText(context, "No app available to export CSV.", Toast.LENGTH_SHORT).show()
        }
    }

    fun buildCsvForEnquiries(enquiries: List<EnquiryEntity>): String {
        fun esc(v: String): String = "\"${v.replace("\"", "\"\"")}\""
        val header = listOf(
            "Reference", "CreatedAt", "Status", "ServiceType", "FullName", "Company",
            "WhatsApp", "Email", "Country", "Origin", "CollectionCity",
            "Destination", "DeliveryCity", "GoodsType", "Weight", "Dimensions",
            "Packages", "EstimatedValue", "SeaOption", "RoadReq", "SpecialInstructions"
        ).joinToString(",")
        val rows = enquiries.map { e ->
            listOf(
                e.referenceNumber,
                e.createdAtIso,
                e.status,
                e.serviceType,
                e.fullName,
                e.companyName,
                e.whatsappNumber,
                e.email,
                e.country,
                e.originCountry,
                e.collectionCity,
                e.destinationCountry,
                e.deliveryCity,
                e.goodsType,
                e.estimatedWeight,
                e.cargoDimensions,
                e.numberOfPackages,
                e.estimatedValue,
                e.seaFreightOption,
                e.roadVehicleRequirements,
                e.specialInstructions
            ).joinToString(",") { esc(it) }
        }
        return (listOf(header) + rows).joinToString("\n")
    }
}
