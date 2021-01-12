package com.inspiredcoda.whatsappstatusgrabber.utils.callbacks

import java.io.File

interface StatusMediaInterface {

    fun onVideoFileSelected(file: File)

    fun onImageFileSelected(file: File)

}