package com.vlk.stocktracker.data.remote

import com.vlk.stocktracker.data.model.StockItem
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

class MockStockApiService @Inject constructor() : StockApiService {

    private val allStockItems = mutableListOf<StockItem>()

    init {
        val symbols = listOf(
            "AAPL" to "Apple",
            "MSFT" to "Microsoft",
            "GOOG" to "Google",
            "ALGN" to "Align Technology",
            "SSG" to "Shinsegae",
            "AMZN" to "Amazon",
            "TSLA" to "Tesla",
            "NFLX" to "Netflix",
            "META" to "Meta",
            "NVDA" to "NVIDIA"
        )
        symbols.forEach { (symbol, name) ->
            allStockItems.add(StockItem(symbol, name, generateRandomPrice()))
        }
    }

    private fun generateRandomPrice(): Float {
        return (Random.nextFloat() * 10 * 10).roundToInt() / 100.0f + 50
    }

    private fun updateStockList() {
        allStockItems.forEach { stockItem ->
            stockItem.price += (Random.nextFloat() * 10 - 5)
        }

    }

    override suspend fun getTopFiveStock(): ApiResponse<List<StockItem>> {
        updateStockList()
        return ApiResponse(
            code = 200,
            message = "success",
            data = allStockItems.sortedByDescending { it.price }.take(5)
        )
    }
}
