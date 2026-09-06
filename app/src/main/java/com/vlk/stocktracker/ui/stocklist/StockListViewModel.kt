package com.vlk.stocktracker.ui.stocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlk.stocktracker.data.repository.StockRepository
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val previousStockPriceMap = mutableMapOf<String, Float>()

    private val _uiState = MutableStateFlow<StockListUiState>(StockListUiState.Loading)
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    fun refreshStockList() {
        viewModelScope.launch {
            val updatedState = getTopFiveStockList()
//            _stockList.value = updatedList
//            _lastUpdatedDate.value = LocalDateTime.now()
            _uiState.value = updatedState
        }
    }

    private suspend fun getTopFiveStockList(): StockListUiState {
        try {
            val data = stockRepository.getTopFiveStock()
            return StockListUiState.Success(
                data.map {
                    val previousPrice = previousStockPriceMap[it.id]
                    val priceTrend = when {
                        previousPrice == null -> PriceTrend.NEUTRAL
                        it.price > previousPrice
                            -> PriceTrend.INCREASING

                        it.price < previousPrice -> PriceTrend.DECREASING
                        else -> PriceTrend.NEUTRAL
                    }
                    previousStockPriceMap[it.id] = it.price
                    StockUiItem(it.id, it.name, "€%.2f".format(it.price), priceTrend)
                },
                LocalDateTime.now()
            )
        } catch (e: Exception) {
            return StockListUiState.Error(e.localizedMessage ?: "Failed to Load Data")
        }
    }

    init {
        refreshStockList()
    }
}