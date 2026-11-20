package com.example.cosmicwindow.core.di

import android.content.Context
import androidx.room3.Room
import com.example.cosmicwindow.data.local.CosmicWindowDatabase
import com.example.cosmicwindow.data.local.SavedItemDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context,
    ): CosmicWindowDatabase =
        Room
            .databaseBuilder(
                context,
                CosmicWindowDatabase::class.java,
                "cosmic_window.db",
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSavedItemDao(database: CosmicWindowDatabase): SavedItemDAO = database.savedItemDAO()
}
