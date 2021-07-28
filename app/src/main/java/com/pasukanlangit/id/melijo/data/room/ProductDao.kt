package com.pasukanlangit.id.melijo.data.room

import androidx.room.*
import com.pasukanlangit.id.melijo.data.network.model.response.ProductItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(productItem: ProductItem)

    @Update
    suspend fun updateProduct(productItem: ProductItem)

    @Delete
    suspend fun deleteProduct(productItem: ProductItem)

    @Query("SELECT * FROM product WHERE ownerId = :ownerId")
    fun getAllProductFromCart(ownerId: Int) : Flow<List<ProductItem>>
}