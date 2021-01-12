package com.inspiredcoda.whatsappstatusgrabber.utils.callbacks

import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.OnStoragePermissionCallback

interface StatusGrabberInterface {

    fun requestStoragePermissions(onStoragePermissionCallback: OnStoragePermissionCallback)

    fun hasStoragePermission(): Boolean






}