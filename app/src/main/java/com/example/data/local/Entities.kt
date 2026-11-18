package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enquiries")
data class EnquiryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val referenceNumber: String,
    val fullName: String,
    val companyName: String,
    val whatsappNumber: String,
    val email: String,
    val country: String,
    val originCountry: String,
    val collectionCity: String,
    val destinationCountry: String,
    val deliveryCity: String,
    val collectionAddress: String,
    val deliveryAddress: String,
    val goodsType: String,
    val cargoDescription: String,
    val estimatedWeight: String,
    val cargoDimensions: String,
    val numberOfPackages: String,
    val estimatedValue: String,
    val serviceType: String,
    val seaFreightOption: String,
    val roadVehicleRequirements: String,
    val specialInstructions: String,
    val attachmentDisplayName: String,
    val attachmentUriString: String,
    val status: String,
    val cloudSyncState: String,
    val createdAtEpochMs: Long,
    val createdAtIso: String
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val categoryId: String,
    val description: String,
    val variations: String,
    val suppliedPriceText: String,
    val imageDrawableKey: String,
    val customImageUri: String,
    val isVisible: Boolean,
    val createdAtEpochMs: Long
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageDrawableKey: String
)

@Entity(tableName = "business_settings")
data class BusinessSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val businessName: String,
    val whatsappNumber: String,
    val address: String,
    val emailPlaceholder: String,
    val phonePlaceholder: String,
    val regNumberPlaceholder: String,
    val vatNumberPlaceholder: String,
    val notificationEmailPlaceholder: String,
    val instagramPlaceholder: String,
    val facebookPlaceholder: String,
    val linkedinPlaceholder: String,
    val heroHeadline: String,
    val heroSubheading: String,
    val aboutDescription: String,
    val sadcConfigSerialized: String
)
