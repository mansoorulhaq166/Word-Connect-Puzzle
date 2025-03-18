package com.wordgame.wordpuzzles.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wordgame.wordpuzzles.data.chapter.Chapter
import com.wordgame.wordpuzzles.data.chapter.ChapterDao
import com.wordgame.wordpuzzles.data.solution.Solution
import com.wordgame.wordpuzzles.data.solution.SolutionDao
import com.wordgame.wordpuzzles.data.level.Level
import com.wordgame.wordpuzzles.data.level.LevelDao

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