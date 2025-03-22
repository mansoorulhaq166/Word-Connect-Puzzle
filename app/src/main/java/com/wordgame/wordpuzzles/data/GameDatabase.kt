package com.wordgame.wordpuzzles.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wordgame.wordpuzzles.data.local.entities.Chapter
import com.wordgame.wordpuzzles.data.local.dao.ChapterDao
import com.wordgame.wordpuzzles.data.local.entities.Solution
import com.wordgame.wordpuzzles.data.local.dao.SolutionDao
import com.wordgame.wordpuzzles.data.local.entities.Level
import com.wordgame.wordpuzzles.data.local.dao.LevelDao

@Database(
    entities = [
        Level::class,
        Solution::class,
        Chapter::class
    ], version = 1, exportSchema = false
)
abstract class GameDatabase:RoomDatabase() {
    abstract fun getLevelDao():LevelDao
    abstract fun getSolutionDao():SolutionDao

    abstract fun getChapterDao():ChapterDao
}