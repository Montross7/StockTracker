package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import com.vlk.stocktracker.ui.theme.StockTrackerTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StockListScreen(
) {
    val stockItems = listOf(
        StockUiItem("AAPL", "Apple", "$100", PriceTrend.INCREASING),
        StockUiItem("MSFT", "Microsoft", "$200", PriceTrend.DECREASING),
        StockUiItem("GOOG", "Google", "$300", PriceTrend.NEUTRAL),
        StockUiItem("ALGN", "Align Technology", "$400", PriceTrend.INCREASING),
        StockUiItem("SSG", "Shinsegae", "$240", PriceTrend.DECREASING),
    )

    val lastUpdatedDate = LocalDateTime.now()

    Scaffold(modifier = Modifier.background(color = Color.White)) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Row(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()) {
                Text(text = "Stock List")
                Spacer(modifier = Modifier.weight(1.0f))
                Text(text = "Last Updated: ${lastUpdatedDate.format(DateTimeFormatter.ofPattern("YYYY/MM/DD HH:mm:ss"))}")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(items = stockItems, key = { it.id }) { item ->
                    StockListItem(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RectangleShape
                            )
                            .animateItem(
                                fadeInSpec = tween(300),
                                fadeOutSpec = tween(300),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )

                            ),
                        name = item.name,
                        price = item.price,
                        itemState = item.priceTrend
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    StockTrackerTheme {
        StockListScreen()
    }
}