package com.wordgame.wordpuzzles.domain.models

import androidx.room.Embedded
import androidx.room.Relation
import com.wordgame.wordpuzzles.data.local.entities.Chapter
import com.wordgame.wordpuzzles.data.local.entities.Level

data class ChapterWithLevel(
    @Embedded
    val chapter: Chapter,

    @Relation(
        parentColumn = "id",
        entityColumn = "chapter_id"
    )
    val level: Level?
)