package com.vlk.stocktracker.data.repository

import com.vlk.stocktracker.data.model.StockItem
import com.vlk.stocktracker.data.remote.StockApiService
import javax.inject.Inject


class StockRepositoryImpl @Inject constructor(
    private val stockApiService: StockApiService
) : StockRepository {

    override suspend fun getTopFiveStock(): List<StockItem> {
        try {
            return stockApiService.getTopFiveStock().data ?: emptyList()

        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}