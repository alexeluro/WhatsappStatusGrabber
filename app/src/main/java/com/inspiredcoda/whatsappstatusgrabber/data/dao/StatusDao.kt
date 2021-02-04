package com.inspiredcoda.whatsappstatusgrabber.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status

@Dao
interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileName(fileName: Status)

    @Query("DELETE FROM status WHERE file_name = :fileName")
    suspend fun deleteFileName(fileName: String)

    @Update
    suspend fun updateFileName(newName: Status)

    @Query("SELECT * FROM status")
    suspend fun getAllFileNames(): List<Status>

}