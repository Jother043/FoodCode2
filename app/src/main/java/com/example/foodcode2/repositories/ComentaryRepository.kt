package com.example.foodcode2.repositories

import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.db.ComentaryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComentaryRepository (
    private val comentsDao: ComentaryDao,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) {
        suspend fun insertComent(coment:UserComentary) = withContext(ioDispatcher){
            return@withContext comentsDao.insert(coment)
        }

        suspend fun getFoodComents(id:String) : List<UserComentary> = withContext(ioDispatcher) {
            return@withContext comentsDao.getFoodComents(id)
        }
}