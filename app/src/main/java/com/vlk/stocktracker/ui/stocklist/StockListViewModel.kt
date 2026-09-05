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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _stockList = MutableStateFlow<List<StockUiItem>>(emptyList())
    val stockList: StateFlow<List<StockUiItem>> = _stockList.asStateFlow()

    private val _lastUpdatedDate = MutableStateFlow<LocalDateTime>(LocalDateTime.now())
    val lastUpdatedDate: StateFlow<LocalDateTime> = _lastUpdatedDate.asStateFlow()


    private val previousStockPriceMap = mutableMapOf<String, Float>()

    suspend fun getTopFiveStockList(): List<StockUiItem> {
        val data = stockRepository.getTopFiveStock()
        return data.map {
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
        }
    }

    init {
        viewModelScope.launch {
            val updatedList = getTopFiveStockList()
            _stockList.value = updatedList
            _lastUpdatedDate.value = LocalDateTime.now()
        }
    }
}