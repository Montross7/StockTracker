package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import com.vlk.stocktracker.ui.theme.Green
import com.vlk.stocktracker.ui.theme.Red
import com.vlk.stocktracker.ui.theme.StockTrackerTheme
import kotlin.math.abs

@Composable
fun StockListItem(
    modifier: Modifier = Modifier, item: StockUiItem
) {
    val color = when (item.priceTrend) {
        PriceTrend.DECREASING -> Red
        PriceTrend.INCREASING -> Green
        else -> Color.Black
    }

    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 500),
        label = "priceColorAnimation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            item.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "€%.2f".format(item.price),
                color = animatedColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if (item.priceChange != 0f) {
                val triangle = if (item.priceChange > 0) "▲" else "▼"
                Text(
                    "%s €%.2f (%.2f%%)".format(
                        triangle,
                        abs(item.priceChange),
                        abs(item.priceChangePercentage)
                    ),
                    color = animatedColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemPreview() {
    StockTrackerTheme {
        StockListItem(item = StockUiItem("AAPL", "Apple", 100f, PriceTrend.NEUTRAL, 0f, 0f))
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemDecreasePreview() {
    StockTrackerTheme {
        StockListItem(item = StockUiItem("AAPL", "Apple", 100f, PriceTrend.DECREASING, -10f, -20f))
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemIncreasingPreview() {
    StockTrackerTheme {
        StockListItem(item = StockUiItem("AAPL", "Apple", 100f, PriceTrend.INCREASING, 10f, 20f))
    }
}