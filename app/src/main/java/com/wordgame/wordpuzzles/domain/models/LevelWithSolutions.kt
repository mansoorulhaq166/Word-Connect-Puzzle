package com.wordgame.wordpuzzles.domain.models

import androidx.room.Embedded
import androidx.room.Relation
import com.wordgame.wordpuzzles.data.local.entities.Level
import com.wordgame.wordpuzzles.data.local.entities.Solution

data class LevelWithSolutions(
    @Embedded
    val level: Level,

    @Relation(
        parentColumn = "id",
        entityColumn = "level_id"
    )
    val solutions:List<Solution>
)