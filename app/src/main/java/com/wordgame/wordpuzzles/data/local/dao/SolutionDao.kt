package com.wordgame.wordpuzzles.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.wordgame.wordpuzzles.data.local.entities.Solution

@Dao
interface SolutionDao {
    @Query("SELECT * FROM wordapp_solution WHERE level_id = :levelid")
    suspend fun list(levelid:String):List<Solution>

    @Query("SELECT solution_details FROM wordapp_solution WHERE solution_word = :solutionWord")
    suspend fun details(solutionWord:String):String?
}