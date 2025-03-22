package com.wordgame.wordpuzzles.data.repository

import com.wordgame.wordpuzzles.data.local.dao.SolutionDao
import javax.inject.Inject

class SolutionRepo @Inject constructor(val dao: SolutionDao) {
    suspend fun list(levelid:String) = dao.list(levelid)
    suspend fun details(solutionWord:String) = dao.details(solutionWord)
}