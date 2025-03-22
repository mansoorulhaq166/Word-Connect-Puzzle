package com.wordgame.wordpuzzles.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.wordgame.wordpuzzles.data.local.entities.Chapter

@Dao
interface ChapterDao {

    @Query("SELECT * FROM wordapp_chapter")
    suspend fun getAllChapters(): List<Chapter>

    @Query("SELECT * FROM wordapp_chapter WHERE chapter_completed = :completed")
    suspend fun listChapters(completed: Int): List<Chapter>

    @Update
    suspend fun update(chapter: Chapter)

    @Query("SELECT * FROM wordapp_chapter WHERE chapter_completed = :chaptercompleted ORDER BY id ASC")
    suspend fun getChapter(chaptercompleted: Boolean): Chapter?

}