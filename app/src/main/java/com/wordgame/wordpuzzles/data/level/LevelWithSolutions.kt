package com.wordgame.wordpuzzles.data.level

import androidx.room.Embedded
import androidx.room.Relation
import com.wordgame.wordpuzzles.data.solution.Solution

data class LevelWithSolutions(
    @Embedded
    val level:Level,

    @Relation(
        parentColumn = "id",
        entityColumn = "level_id"
    )
    val solutions:List<Solution>
)
