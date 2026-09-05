package com.vlk.stocktracker.data.remote

import com.vlk.stocktracker.data.model.StockItem
import retrofit2.http.GET

data class ApiResponse<T>(val code: Int, val message: String, val data: T?)

interface StockApiService {

    @GET("stock/topFive")
    suspend fun getTopFiveStock(): ApiResponse<List<StockItem>>


}