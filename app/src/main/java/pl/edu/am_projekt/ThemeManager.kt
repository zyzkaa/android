package pl.edu.am_projekt

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

object ThemeManager {

    fun applyTheme(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val theme = prefs.getString("pref_theme", "system")

        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun updateTheme(themeValue: String) {
        when (themeValue) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}