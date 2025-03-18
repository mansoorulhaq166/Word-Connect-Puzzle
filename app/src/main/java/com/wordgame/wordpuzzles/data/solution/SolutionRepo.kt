package com.wordgame.wordpuzzles.data.solution

import javax.inject.Inject

class SolutionRepo @Inject constructor(val dao:SolutionDao) {
    suspend fun list(levelid:String) = dao.list(levelid)
    suspend fun details(solutionWord:String) = dao.details(solutionWord)
}