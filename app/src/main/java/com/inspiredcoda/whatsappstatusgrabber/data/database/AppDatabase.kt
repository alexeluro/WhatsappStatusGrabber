package com.inspiredcoda.whatsappstatusgrabber.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inspiredcoda.whatsappstatusgrabber.data.dao.StatusDao
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Database.DATABASE_NAME

@Database(
    entities = [Status::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (INSTANCE == null) {
                @Volatile
                @Synchronized
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE!!

            } else {
                INSTANCE!!
            }
        }
    }


    abstract fun getStatusDao(): StatusDao


}