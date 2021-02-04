package com.inspiredcoda.whatsappstatusgrabber.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status")
data class Status(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "file_name")
    var fileName: String,

    @ColumnInfo(name = "former_name")
    var formerName: String,

    @ColumnInfo(name = "is_new_status")
    var isNewStatus: Boolean = true

)