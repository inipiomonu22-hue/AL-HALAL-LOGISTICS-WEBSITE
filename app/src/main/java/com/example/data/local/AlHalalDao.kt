package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlHalalDao {
    // Enquiries
    @Query("SELECT * FROM enquiries ORDER BY createdAtEpochMs DESC")
    fun observeAllEnquiries(): Flow<List<EnquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity): Long

    @Query("UPDATE enquiries SET status = :status WHERE id = :id")
    suspend fun updateEnquiryStatus(id: Long, status: String)

    // Products
    @Query("SELECT * FROM products ORDER BY createdAtEpochMs DESC")
    fun observeAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isVisible = 1 ORDER BY createdAtEpochMs DESC")
    fun observeVisibleProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int

    // Categories
    @Query("SELECT * FROM categories")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun countCategories(): Int

    // Business Settings
    @Query("SELECT * FROM business_settings WHERE id = 1")
    fun observeBusinessSettings(): Flow<BusinessSettingsEntity?>

    @Query("SELECT * FROM business_settings WHERE id = 1")
    suspend fun getBusinessSettingsOnce(): BusinessSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBusinessSettings(settings: BusinessSettingsEntity)
}
