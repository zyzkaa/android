package pl.edu.am_projekt

import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.applyTheme(this)
    }
}