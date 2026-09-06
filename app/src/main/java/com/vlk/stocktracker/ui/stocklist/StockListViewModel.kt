package com.vlk.stocktracker.ui.stocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlk.stocktracker.data.model.StockItem
import com.vlk.stocktracker.data.repository.StockRepository
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val previousStockPriceMap = mutableMapOf<String, Float>()

    private val _uiState = MutableStateFlow<StockListUiState>(StockListUiState.Loading)
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    fun refreshStockList(isForceRefresh: Boolean = false) {
        viewModelScope.launch {
            val current = _uiState.value
            if (current is StockListUiState.Success && isForceRefresh) {
                _uiState.value = current.copy(isRefreshing = true)
            }

            val result = fetchTopFiveStockList()

            _uiState.value = result
        }
    }

    private suspend fun fetchTopFiveStockList(): StockListUiState {
        try {
            val data = stockRepository.getTopFiveStock()
            return mapToUiState(data)
        } catch (e: Exception) {
            return StockListUiState.Error(e.localizedMessage ?: "Failed to Load Data")
        }
    }

    private fun mapToUiState(data: List<StockItem>): StockListUiState {
        return StockListUiState.Success(
            data.map {
                val previousPrice = previousStockPriceMap[it.id]
                val priceChange = if (previousPrice != null) it.price - previousPrice else 0f
                val priceChangePercentage =
                    if (previousPrice != null && previousPrice != 0f) (priceChange / previousPrice) * 100f
                    else 0f

                val priceTrend = when {
                    priceChange > 0f -> PriceTrend.INCREASING

                    priceChange < 0f -> PriceTrend.DECREASING
                    else -> PriceTrend.NEUTRAL
                }

                previousStockPriceMap[it.id] = it.price
                StockUiItem(
                    it.id,
                    it.name,
                    it.price,
                    priceTrend,
                    priceChange,
                    priceChangePercentage
                )
            }, LocalDateTime.now()
        )
    }

    init {
//        viewModelScope.launch {
//            stockRepository.getStockUpdates().catch { e ->
//                _uiState.value = StockListUiState.Error(e.localizedMessage ?: "Stream Error")
//            }.collect { data ->
//                _uiState.value = mapToUiState(data)
//            }
//        }
        refreshStockList()
    }
}