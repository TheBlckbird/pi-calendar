package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.core.db.PiDatabase
import com.louisweigel.pi_calendar.core.db.getDatabaseBuilder
import com.louisweigel.pi_calendar.core.db.getRoomDatabase
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val database: PiDatabase by lazy { getRoomDatabase(getDatabaseBuilder()) }
}

actual fun getPlatform(): Platform = IOSPlatform()