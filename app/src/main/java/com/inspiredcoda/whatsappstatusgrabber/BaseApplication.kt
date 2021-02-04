package com.inspiredcoda.whatsappstatusgrabber

import android.app.Application
import com.facebook.stetho.Stetho

class BaseApplication: Application() {

    override fun onCreate() {
        Stetho.initializeWithDefaults(this)
        super.onCreate()
    }

}