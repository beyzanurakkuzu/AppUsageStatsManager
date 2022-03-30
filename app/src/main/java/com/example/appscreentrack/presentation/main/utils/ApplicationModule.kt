package com.example.appscreentrack.presentation.main.utils

import android.content.Context
import androidx.room.Room
import com.example.appscreentrack.data.database.AppDatabase
import com.example.appscreentrack.data.database.StatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    @Provides
    fun provideStatsDao(
        database: AppDatabase
    ): StatsDao {
        return database.statsDao()
    }
}