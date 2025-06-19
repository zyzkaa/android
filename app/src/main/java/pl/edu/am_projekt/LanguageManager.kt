package pl.edu.am_projekt

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale
import androidx.core.content.edit

object LanguageManager {

    fun applyLanguage(context: Context): Context {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val language = prefs.getString("pref_language", Locale.getDefault().language) ?: "en"
        return setLocale(context, language)
    }

    fun updateLanguage(context: Context, languageCode: String): Context {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit { putString("pref_language", languageCode) }
        return setLocale(context, languageCode)
    }

    private fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}