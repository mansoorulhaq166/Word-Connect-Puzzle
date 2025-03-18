package com.wordgame.wordpuzzles.data.solution

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SolutionDao {
    @Query("SELECT * FROM wordapp_solution WHERE level_id = :levelid")
    suspend fun list(levelid:String):List<Solution>

    @Query("SELECT solution_details FROM wordapp_solution WHERE solution_word = :solutionWord")
    suspend fun details(solutionWord:String):String?
}