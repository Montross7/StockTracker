package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.ui.theme.Green
import com.vlk.stocktracker.ui.theme.Red
import com.vlk.stocktracker.ui.theme.StockTrackerTheme

@Composable
fun StockListItem(
    name: String,
    price: String,
    itemState: PriceTrend = PriceTrend.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val color = when (itemState) {
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
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            price,
            color = animatedColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
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