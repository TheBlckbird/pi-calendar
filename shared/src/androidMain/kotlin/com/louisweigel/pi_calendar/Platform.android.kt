package com.louisweigel.pi_calendar

import android.os.Build
import com.louisweigel.pi_calendar.PiApplication.Companion.appContext
import com.louisweigel.pi_calendar.core.db.PiDatabase
import com.louisweigel.pi_calendar.core.db.getDatabaseBuilder
import com.louisweigel.pi_calendar.core.db.getRoomDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val database: PiDatabase by lazy {
        getRoomDatabase(getDatabaseBuilder(appContext))
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
