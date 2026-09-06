package com.vlk.stocktracker.ui.stocklist

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlk.stocktracker.domain.model.PriceTrend
import com.vlk.stocktracker.domain.model.StockUiItem
import com.vlk.stocktracker.ui.theme.StockTrackerTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StockListScreen(
    stockItems: List<StockUiItem> = emptyList(),
    lastUpdatedDate: LocalDateTime = LocalDateTime.now(),
    onRefresh: () -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()
    val dragOffsetY = remember { Animatable(-110f) }
    val rotation = remember { Animatable(0f) }
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (dragOffsetY.value > 40f && !isRefreshing) {
                            coroutineScope.launch {
                                isRefreshing = true
                                onRefresh()
                                rotation.animateTo(
                                    targetValue = rotation.value + 540f,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = LinearEasing
                                    )
                                )
                                dragOffsetY.animateTo(-110f, tween(500))
                                rotation.snapTo(0f)
                                isRefreshing = false
                            }
                        } else if (!isRefreshing) {
                            coroutineScope.launch {
                                dragOffsetY.animateTo(-110f, tween(300))
                                rotation.animateTo(0f, tween(300))
                            }
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        if (!isRefreshing) {
                            change.consume()
                            val newY = (dragOffsetY.value + dragAmount).coerceIn(-110f, 50f)
                            coroutineScope.launch {
                                dragOffsetY.snapTo(newY)
                                rotation.snapTo(newY * 5f)
                            }
                        }
                    }
                )
            }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {
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
                    Text(
                        text = "Last Updated: ${
                            lastUpdatedDate.format(
                                DateTimeFormatter.ofPattern(
                                    "YYYY/MM/DD HH:mm:ss"
                                )
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(items = stockItems, key = { it.id }) { item ->
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
                            name = item.name,
                            price = item.price,
                            itemState = item.priceTrend
                        )
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = dragOffsetY.value.dp)
                    .graphicsLayer(rotationZ = rotation.value)
                    .size(48.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    val stockItems = listOf(
        StockUiItem("AAPL", "Apple", "$100", PriceTrend.INCREASING),
        StockUiItem("MSFT", "Microsoft", "$200", PriceTrend.DECREASING),
        StockUiItem("GOOG", "Google", "$300", PriceTrend.NEUTRAL),
        StockUiItem("ALGN", "Align Technology", "$400", PriceTrend.INCREASING),
        StockUiItem("SSG", "Shinsegae", "$240", PriceTrend.DECREASING),
    )
    StockTrackerTheme {
        StockListScreen(
            stockItems = stockItems,
            lastUpdatedDate = LocalDateTime.now()
        )
    }
}