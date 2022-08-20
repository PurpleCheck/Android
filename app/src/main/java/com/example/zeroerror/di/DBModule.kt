package com.example.zeroerror.di

import android.content.Context
import com.example.zeroerror.data.persistence.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)!!

    @Singleton
    @Provides
    fun provideInspectDao(appDatabase: AppDatabase): InspectDao = appDatabase.InspectDao()

    @Singleton
    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao = appDatabase.orderDao()
}