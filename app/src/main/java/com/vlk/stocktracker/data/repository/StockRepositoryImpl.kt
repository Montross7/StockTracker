package com.vlk.stocktracker.data.repository

import com.vlk.stocktracker.data.model.StockItem
import com.vlk.stocktracker.data.remote.StockApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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

    override fun getStockUpdates(): Flow<List<StockItem>> =
        stockApiService.getStockUpdateStream()
            .map { it.data ?: emptyList() }
            .catch { e -> throw Exception(e.message) }
}