package com.vlk.stocktracker.data.remote

import com.vlk.stocktracker.data.model.StockItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MockStockApiService @Inject constructor() : StockApiService {

    private var currentPrices: List<StockItem> = generateInitialItems()

    private fun generateInitialItems(): List<StockItem> {
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
        return symbols.map { (symbol, name) ->
            StockItem(id = symbol, name = name, price = Random.nextFloat() * 10f + 50f)
        }
    }

    private fun applyPriceFluctuationRandomly(items: List<StockItem>): List<StockItem> {
        val n = Random.nextInt(1, items.size + 1)
        val indicesToUpdate = items.indices.shuffled().take(n).toSet()

        return items.mapIndexed { index, item ->
            if (index in indicesToUpdate) {
                val delta = Random.nextFloat() * 10f - 5f
                val newPrice = (item.price + delta).coerceAtLeast(0f)
                item.copy(price = newPrice)
            } else {
                item
            }
        }
    }

    private fun applyPriceFluctuation(items: List<StockItem>): List<StockItem> {
        return items.map { item ->
            val delta = Random.nextFloat() * 10f - 5f
            val newPrice = (item.price + delta).coerceAtLeast(0f)
            item.copy(price = newPrice)
        }
    }


    private fun topFive(items: List<StockItem>): List<StockItem> =
        items.sortedByDescending { it.price }.take(5)

    override suspend fun getTopFiveStock(): ApiResponse<List<StockItem>> {
        delay(1000.milliseconds) // simulate network latency
        currentPrices = applyPriceFluctuation(currentPrices)
        return ApiResponse(code = 200, message = "success", data = topFive(currentPrices))
    }

    override fun getStockUpdateStream(): Flow<ApiResponse<List<StockItem>>> = flow {
        while (true) {
            currentPrices = applyPriceFluctuation(currentPrices)
            emit(ApiResponse(code = 200, message = "success", data = topFive(currentPrices)))
            delay(Random.nextLong(2000, 5000).milliseconds)
        }
    }
}