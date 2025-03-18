package com.wordgame.wordpuzzles.data.chapter

import androidx.room.Embedded
import androidx.room.Relation
import com.wordgame.wordpuzzles.data.level.Level

data class ChapterWithLevel(
    @Embedded
    val chapter:Chapter,

    @Relation(
        parentColumn = "id",
        entityColumn = "chapter_id"
    )
    val level:Level?
)