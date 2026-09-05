package com.vlk.stocktracker.data.repository

import com.vlk.stocktracker.data.model.StockItem


interface StockRepository {
    suspend fun getTopFiveStock(): List<StockItem>

}