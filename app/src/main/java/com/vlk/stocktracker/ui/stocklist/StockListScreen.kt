package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import com.vlk.stocktracker.ui.theme.Red
import com.vlk.stocktracker.ui.theme.StockTrackerTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StockListScreen(
    uiState: StockListUiState,
    onRefresh: () -> Unit = {}
) {

    val isRefreshing = (uiState as? StockListUiState.Success)?.isRefreshing ?: false

    Scaffold { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stock List",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    when (uiState) {
                        is StockListUiState.Loading -> {}
                        is StockListUiState.Error -> {}
                        is StockListUiState.Success -> {
                            Text(
                                text = "Last Updated: ${
                                    uiState.lastUpdatedDate.format(
                                        DateTimeFormatter.ofPattern(
                                            "yyyy/MM/DD HH:mm:ss"
                                        )
                                    )
                                }",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                when (uiState) {
                    is StockListUiState.Loading -> com.vlk.stocktracker.ui.common.LoadingIndicator()
                    is StockListUiState.Error -> Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            uiState.message,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            color = Red,
                            fontSize = 20.sp
                        )
                    }

                    is StockListUiState.Success -> LazyColumn(
                        modifier = Modifier
                    ) {
                        items(items = uiState.items, key = { it.id }) { item ->
                            StockListItem(
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = tween(300),
                                        fadeOutSpec = tween(300),
                                        placementSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )

                                    ),
                                item = item
                            )
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    val stockItems = listOf(
        StockUiItem("AAPL", "Apple", 100f, PriceTrend.INCREASING, 10f, 10f),
        StockUiItem("MSFT", "Microsoft", 200f, PriceTrend.DECREASING, -10f, -10f),
        StockUiItem("GOOG", "Google", 300f, PriceTrend.NEUTRAL, 0f, 0f),
        StockUiItem("ALGN", "Align Technology", 400f, PriceTrend.INCREASING, 5f, 10f),
        StockUiItem("SSG", "Shinsegae", 240f, PriceTrend.DECREASING, -3.45f, 12.23f),
    )
    StockTrackerTheme {
        StockListScreen(
            uiState = StockListUiState.Success(stockItems, LocalDateTime.now()),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StockListScreenErrorPreview() {
    StockTrackerTheme {
        StockListScreen(
            uiState = StockListUiState.Error("Failed To Fetch Data"),
        )
    }
}