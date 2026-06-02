package com.louisweigel.pi_calendar

import android.app.Application
import android.content.Context

class PiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}