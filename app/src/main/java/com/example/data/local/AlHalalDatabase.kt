package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ProductCategoryType
import com.example.data.model.SadcCountryConfig

@Database(
    entities = [
        EnquiryEntity::class,
        ProductEntity::class,
        CategoryEntity::class,
        BusinessSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AlHalalDatabase : RoomDatabase() {
    abstract fun alHalalDao(): AlHalalDao

    companion object {
        @Volatile
        private var INSTANCE: AlHalalDatabase? = null

        fun getInstance(context: Context): AlHalalDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AlHalalDatabase::class.java,
                    "al_halal_logistics_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

object SeedData {
    val defaultSadcCountries: List<SadcCountryConfig> = listOf(
        SadcCountryConfig("ZA", "South Africa", "Springs / Gauteng Hub & Ports Coordination", true),
        SadcCountryConfig("BW", "Botswana", "Gaborone & Francistown Cross-Border Corridor", true),
        SadcCountryConfig("NA", "Namibia", "Windhoek & Walvis Bay Transit Corridor", true),
        SadcCountryConfig("ZW", "Zimbabwe", "Beitbridge / Harare Commercial Corridor", true),
        SadcCountryConfig("MZ", "Mozambique", "Maputo & Beira Port Road Link", true),
        SadcCountryConfig("ZM", "Zambia", "Lusaka & Copperbelt Freight Corridor", true),
        SadcCountryConfig("SZ", "Eswatini", "Manzini & Mbabane Regional Route", true),
        SadcCountryConfig("LS", "Lesotho", "Maseru Cross-Border Commercial Route", true),
        SadcCountryConfig("MW", "Malawi", "Lilongwe & Blantyre Corridor (Enquiry Basis)", true),
        SadcCountryConfig("CD", "DRC (Southern)", "Lubumbashi Corridor (Project Basis)", false),
        SadcCountryConfig("TZ", "Tanzania", "Dar es Salaam Link (Partner Coordination)", false)
    )

    fun serializeSadc(list: List<SadcCountryConfig>): String =
        list.joinToString(";;") { "${it.code}|${it.name}|${it.corridorNote}|${it.isServiceEnabled}" }

    fun parseSadc(raw: String): List<SadcCountryConfig> {
        if (raw.isBlank()) return defaultSadcCountries
        val parsed = raw.split(";;").mapNotNull { entry ->
            val parts = entry.split("|")
            if (parts.size >= 4) {
                SadcCountryConfig(
                    code = parts[0],
                    name = parts[1],
                    corridorNote = parts[2],
                    isServiceEnabled = parts[3].toBooleanStrictOrNull() ?: true
                )
            } else null
        }
        return parsed.ifEmpty { defaultSadcCountries }
    }

    fun defaultSettings(): BusinessSettingsEntity = BusinessSettingsEntity(
        id = 1,
        businessName = "AL-HALAL LOGISTICS N TRANS",
        whatsappNumber = "+27 74 076 7068",
        address = "281 Beverly Street, New State Areas, Springs, Gauteng, 1559, South Africa",
        emailPlaceholder = "",
        phonePlaceholder = "",
        regNumberPlaceholder = "",
        vatNumberPlaceholder = "",
        notificationEmailPlaceholder = "",
        instagramPlaceholder = "",
        facebookPlaceholder = "",
        linkedinPlaceholder = "",
        heroHeadline = "GLOBAL LOGISTICS. TRUSTED ISLAMIC SUPPLIES. ONE RELIABLE PARTNER.",
        heroSubheading = "Connecting businesses and communities through international freight forwarding, South African customs clearance, global cargo delivery and quality Islamic products.\n\nWe provide logistics solutions across international markets and Southern Africa while making Islamic clothing and general Islamic supplies accessible to customers worldwide.",
        aboutDescription = "AL-HALAL LOGISTICS N TRANS is an international logistics and general Islamic supplies company committed to connecting businesses, individuals and communities through dependable cargo transportation and quality Islamic products.\n\nOur logistics operations focus on international freight forwarding, air and sea cargo, South African customs clearance coordination, global door-to-door delivery and regional road freight across Southern Africa.\n\nThrough our Islamic supplies division, we also work to make Islamic clothing, modest fashion, prayer accessories and general Islamic lifestyle products accessible to customers worldwide.\n\nOur business is guided by integrity, transparency, professionalism and a commitment to customer satisfaction.",
        sadcConfigSerialized = serializeSadc(defaultSadcCountries)
    )

    fun defaultCategories(): List<CategoryEntity> = listOf(
        CategoryEntity(
            id = ProductCategoryType.MENS_CLOTHING.id,
            name = ProductCategoryType.MENS_CLOTHING.displayName,
            description = "Thobes, Jubbas, Kufi caps, Prayer caps, traditional Islamic garments, and men's modest clothing accessories.",
            imageDrawableKey = "clothing"
        ),
        CategoryEntity(
            id = ProductCategoryType.WOMENS_CLOTHING.id,
            name = ProductCategoryType.WOMENS_CLOTHING.displayName,
            description = "Abayas, Hijabs, Khimars, Jilbabs, modest dresses, and Islamic women's accessories.",
            imageDrawableKey = "clothing"
        ),
        CategoryEntity(
            id = ProductCategoryType.ISLAMIC_ACCESSORIES.id,
            name = ProductCategoryType.ISLAMIC_ACCESSORIES.displayName,
            description = "Prayer mats, Tasbeeh, Islamic decorative items, prayer accessories, and Islamic lifestyle products.",
            imageDrawableKey = "accessories"
        ),
        CategoryEntity(
            id = ProductCategoryType.BOOKS_AND_EDUCATION.id,
            name = ProductCategoryType.BOOKS_AND_EDUCATION.displayName,
            description = "Islamic books, learning resources, and educational materials for families, students, and institutions.",
            imageDrawableKey = "accessories"
        )
    )

    // Note: No invented prices or unverified stock claims; prices remain empty ("Enquire for availability & pricing") until supplied by admin.
    fun defaultProducts(now: Long): List<ProductEntity> = listOf(
        ProductEntity(
            name = "Classic Tailored Men's Thobe",
            categoryId = ProductCategoryType.MENS_CLOTHING.id,
            description = "Breathable tailored thobe designed for everyday wear, Jumu'ah, and formal gatherings. Available on enquiry for individual or wholesale orders.",
            variations = "Sizes & fabric shades available on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "clothing",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 8000L
        ),
        ProductEntity(
            name = "Embroidered Men's Jubba & Kufi Cap Set",
            categoryId = ProductCategoryType.MENS_CLOTHING.id,
            description = "Traditional men's jubba paired with a structured kufi prayer cap in coordinated neutral and emerald tones.",
            variations = "Multiple collar styles available on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "clothing",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 7000L
        ),
        ProductEntity(
            name = "Men's Woven Prayer Cap (Kufi)",
            categoryId = ProductCategoryType.MENS_CLOTHING.id,
            description = "Comfortable lightweight kufi and prayer cap collection suitable for daily prayers and bulk community supply.",
            variations = "Standard & stretch-fit options on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "clothing",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 6000L
        ),
        ProductEntity(
            name = "Elegant Everyday Modest Abaya",
            categoryId = ProductCategoryType.WOMENS_CLOTHING.id,
            description = "Flowing, full-coverage modest abaya crafted for comfort, drape, and understated elegance.",
            variations = "Open & closed front options on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "clothing",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 5000L
        ),
        ProductEntity(
            name = "Soft Chiffon & Jersey Hijab Collection",
            categoryId = ProductCategoryType.WOMENS_CLOTHING.id,
            description = "Modest hijabs, khimars, and jilbab garments available in curated earthy, cream, charcoal, and deep emerald palettes.",
            variations = "Single & bulk assortment orders on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "clothing",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 4000L
        ),
        ProductEntity(
            name = "Luxury Woven Geometric Prayer Mat",
            categoryId = ProductCategoryType.ISLAMIC_ACCESSORIES.id,
            description = "Cushioned, finely woven prayer mat featuring traditional geometric borders for home, masjid, or gifting.",
            variations = "Standard & padded gift editions on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "accessories",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 3000L
        ),
        ProductEntity(
            name = "Polished Wooden Tasbeeh (33 / 99 Beads)",
            categoryId = ProductCategoryType.ISLAMIC_ACCESSORIES.id,
            description = "Smooth hand-finished prayer beads with woven tassel, complemented by Islamic lifestyle and home decor items.",
            variations = "33-bead & 99-bead options on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "accessories",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 2000L
        ),
        ProductEntity(
            name = "Islamic Educational & Seerah Book Collection",
            categoryId = ProductCategoryType.BOOKS_AND_EDUCATION.id,
            description = "Curated Islamic books, Arabic learning workbooks, and educational materials for personal study, madrasahs, and libraries.",
            variations = "Individual titles & institutional bundles on enquiry",
            suppliedPriceText = "",
            imageDrawableKey = "accessories",
            customImageUri = "",
            isVisible = true,
            createdAtEpochMs = now - 1000L
        )
    )
}
