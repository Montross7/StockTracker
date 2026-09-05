package com.vlk.stocktracker.di

import com.vlk.stocktracker.data.remote.MockStockApiService
import com.vlk.stocktracker.data.remote.StockApiService
import com.vlk.stocktracker.data.repository.StockRepository
import com.vlk.stocktracker.data.repository.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindStockApiService(
        mockStockApiService: MockStockApiService
    ): StockApiService

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}
