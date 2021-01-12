package com.inspiredcoda.whatsappstatusgrabber.utils

import java.io.File

object FileDirectory {

    fun loadDirectoryFiles(directory: File){

        val allFiles = directory.listFiles()
        if (!allFiles.isNullOrEmpty()){
            for (x in allFiles){
                if (x.isDirectory){
                    loadDirectoryFiles(x)
                }else{
                    val fileName = x.name.toLowerCase()
                    for (y in Constants.VideoConstant.extentions){
                        if (fileName.endsWith(y, true)){
                            // add the file to a list
                            break
                        }
                    }
                }
            }
        }

    }


}