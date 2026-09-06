package com.vlk.stocktracker.data.repository

import com.vlk.stocktracker.data.model.StockItem
import kotlinx.coroutines.flow.Flow


interface StockRepository {
    suspend fun getTopFiveStock(): List<StockItem>

    fun getStockUpdates(): Flow<List<StockItem>>
}