package com.wordgame.wordpuzzles.di

import android.app.Application
import androidx.room.Room
import com.wordgame.wordpuzzles.billing.BillingRepository
import com.wordgame.wordpuzzles.data.GameDatabase
import com.wordgame.wordpuzzles.data.chapter.ChapterDao
import com.wordgame.wordpuzzles.data.solution.SolutionDao
import com.wordgame.wordpuzzles.data.level.LevelDao
import com.wordgame.wordpuzzles.billing.BillingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "wordgame.db"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDatabase(context: Application): GameDatabase {
        return Room.databaseBuilder(
            context,
            GameDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset(DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun providesWordDao(database: GameDatabase): LevelDao {
        return database.getLevelDao()
    }

    @Singleton
    @Provides
    fun providesAnswerDao(database: GameDatabase): SolutionDao {
        return database.getSolutionDao()
    }

    @Singleton
    @Provides
    fun providesChapterDao(database: GameDatabase): ChapterDao {
        return database.getChapterDao()
    }

    @Singleton
    @Provides
    fun providesBillingRepository(context: Application): BillingRepository {
        return BillingRepository(context)
    }

    @Singleton
    @Provides
    fun providesBillingViewModel(billingRepository: BillingRepository): BillingViewModel {
        return BillingViewModel(billingRepository)
    }
}