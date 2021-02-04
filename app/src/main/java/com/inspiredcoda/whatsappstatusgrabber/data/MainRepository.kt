package com.inspiredcoda.whatsappstatusgrabber.data

import androidx.lifecycle.LiveData
import com.inspiredcoda.whatsappstatusgrabber.data.dao.StatusDao
import com.inspiredcoda.whatsappstatusgrabber.data.database.AppDatabase
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status

class MainRepository(
    private var statusDao: StatusDao
) {

    suspend fun saveFileNameToDb(fileName: Status){
        statusDao.insertFileName(fileName)
    }

    suspend fun getAllViewedFileNames(): List<Status>?{
        return statusDao.getAllFileNames()
    }


}