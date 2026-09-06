package com.vlk.stocktracker

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vlk.stocktracker.ui.stocklist.StockListScreen
import com.vlk.stocktracker.ui.stocklist.StockListViewModel

@Composable
fun StockTrackerApp(
    viewModel: StockListViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    StockListScreen(uiState.value, onRefresh = { viewModel.refreshStockList() })
}