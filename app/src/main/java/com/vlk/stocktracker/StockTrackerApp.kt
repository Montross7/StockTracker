package com.vlk.stocktracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.vlk.stocktracker.ui.stocklist.StockListScreen
import com.vlk.stocktracker.ui.stocklist.StockListViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun StockTrackerApp(
    viewModel: StockListViewModel = hiltViewModel(),
) {
    val stockList = viewModel.stockList.collectAsStateWithLifecycle().value
    val lastUpdatedDate = viewModel.lastUpdatedDate.collectAsStateWithLifecycle().value

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, viewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                viewModel.refreshStockList()
                val nextDelay = Random.nextLong(900, 1_001)
                delay(nextDelay.milliseconds)
            }
        }
    }

    StockListScreen(stockList, lastUpdatedDate)
}