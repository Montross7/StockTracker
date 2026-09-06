package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.ui.theme.StockTrackerTheme

@Composable
fun StockListItem(
    name: String,
    price: String,
    itemState: PriceTrend = PriceTrend.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val color = when (itemState) {
        PriceTrend.DECREASING -> Color.Red
        PriceTrend.INCREASING -> Color.Green
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
            .padding(10.dp)
    ) {
        Text(name)
        Spacer(modifier = Modifier.weight(1f))
        Text(price, color = animatedColor)
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemPreview() {
    StockTrackerTheme {
        StockListItem("Android", price = "$100")
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemDecreasePreview() {
    StockTrackerTheme {
        StockListItem("Android", price = "$100", itemState = PriceTrend.DECREASING)
    }
}

@Preview(showBackground = true)
@Composable
fun StockListItemIncreasingPreview() {
    StockTrackerTheme {
        StockListItem("Android", price = "$100", itemState = PriceTrend.INCREASING)
    }
}