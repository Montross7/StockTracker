package com.vlk.stocktracker.ui.stocklist

import com.vlk.stocktracker.domain.model.StockUiItem
import java.time.LocalDateTime

sealed interface StockListUiState {
    data object Loading : StockListUiState

    data class Success(
        val items: List<StockUiItem>,
        val lastUpdatedDate: LocalDateTime,
        val isRefreshing: Boolean = false
    ) : StockListUiState

    data class Error(
        val message: String
    ) : StockListUiState
}