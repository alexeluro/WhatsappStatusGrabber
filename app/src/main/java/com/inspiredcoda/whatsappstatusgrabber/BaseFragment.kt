package com.inspiredcoda.whatsappstatusgrabber

import android.content.Context
import androidx.fragment.app.Fragment
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.OnStoragePermissionCallback
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.RefreshStatusDirectory
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusGrabberInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.UserInterfaceListener
import java.lang.ClassCastException

abstract class BaseFragment : Fragment(), StatusGrabberInterface, UserInterfaceListener, RefreshStatusDirectory {


    private var statusGrabberInterface: StatusGrabberInterface? = null
    private var userInterfaceListener: UserInterfaceListener? = null
    private var refreshStatusListener: RefreshStatusDirectory? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            statusGrabberInterface = context as StatusGrabberInterface
            userInterfaceListener = context as UserInterfaceListener
            refreshStatusListener = context as RefreshStatusDirectory
        } catch (e: ClassCastException) {

        }
    }

    override fun requestStoragePermissions(onStoragePermissionCallback: OnStoragePermissionCallback) {
        statusGrabberInterface?.requestStoragePermissions(onStoragePermissionCallback)
    }

    override fun hasStoragePermission(): Boolean {
        return statusGrabberInterface?.hasStoragePermission()!!
    }

    override fun showActionBar(status: Boolean) {
        userInterfaceListener?.showActionBar(status)
    }

    override fun refreshStatusDirectories() {
        refreshStatusListener?.refreshStatusDirectories()
    }
}