package com.vlk.stocktracker.domain.model

data class StockUiItem(
    val id: String,
    val name: String,
    val price: Float,
    val priceTrend: PriceTrend,
    val priceChange: Float,
    val priceChangePercentage: Float
)