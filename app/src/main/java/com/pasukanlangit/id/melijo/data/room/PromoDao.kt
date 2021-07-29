package com.pasukanlangit.id.melijo.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PromoDao {
    @Insert
    suspend fun insertPromo(promoResultItem: PromoResultItem)

    @Query("DELETE FROM promo")
    suspend fun deleteAllPromo()

    @Query("SELECT * FROM promo")
    fun getMainPromo(): Flow<PromoResultItem>
}