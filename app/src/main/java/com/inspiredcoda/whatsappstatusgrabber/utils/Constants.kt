package com.inspiredcoda.whatsappstatusgrabber.utils

import android.Manifest

class Constants {

    enum class ResultState {
        LOADING, ERROR, SUCCESS
    }

    object FileCategory{
        const val VIEWED_STATUS = 0
        const val SAVED_STATUS = 1
    }

    object VideoConstant {

        val extentions = arrayListOf(
            ".mp4", ".mpeg1", ".mpeg2", ".mpeg3", ".mpeg4", ".avi",
            ".3gp", ".mpg", ".mkv", ".mov"
        )

        const val VIDEO_FILE = "video_player"
        const val VIDEO_SOURCE = "whatsapp_status_grabber_source"

        const val INTENT_WSG_SHARE_MSSG = "whatsapp_status_grabber_mssg"

    }

    object VideoSource {
        const val SAVED_STATUS = "saved_status"
        const val VIEWED_STATUS = "viewed_status"
    }

    object Permissions{
        const val STORAGE_REQUEST_CODE = 1004
        const val PERMISSION_RATIONALE = "You need this permission to use this app"
        const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }


}