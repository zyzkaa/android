package pl.edu.am_projekt.fragment.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.coroutines.launch
import pl.edu.am_projekt.LanguageManager
import pl.edu.am_projekt.R
import pl.edu.am_projekt.ThemeManager
import pl.edu.am_projekt.TimePickerPreference
import pl.edu.am_projekt.TimePreferenceDialogFragment
import pl.edu.am_projekt.activity.AuthActivity
import pl.edu.am_projekt.activity.MainActivity
import pl.edu.am_projekt.model.workout.request.RemindersRequest
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import androidx.core.content.edit

class AppSettingsFragment : PreferenceFragmentCompat()  {
    private val apiService = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings_fragment, rootKey)

        val themePref = findPreference<ListPreference>("pref_theme")
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            ThemeManager.updateTheme(newValue as String)
            true
        }

        val languagePref = findPreference<ListPreference>("pref_language")
        languagePref?.setOnPreferenceChangeListener { _, newValue ->
            LanguageManager.updateLanguage(requireContext(), newValue as String)
            requireActivity().recreate()
            true
        }

        val timePref = findPreference<TimePickerPreference>("pref_schedule_time")
        timePref?.setOnPreferenceChangeListener { _, newValue ->
            saveScheduleToApi(newValue as String, getSelectedDays())
            true
        }

        val daysPref = findPreference<MultiSelectListPreference>("pref_schedule_days")
        daysPref?.setOnPreferenceChangeListener { _, newValue ->
            saveScheduleToApi(getSelectedTime(), (newValue as Set<*>).map { it.toString() })
            true
        }

        val logoutPref = findPreference<Preference>("logout_button")
        logoutPref?.setOnPreferenceClickListener {
            lifecycleScope.launch {
                apiService.logout()

                val sharedPrefs = requireContext()
                    .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPrefs.edit {
                    putBoolean("fcm_sent", false)
                }

                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            true
        }
    }


    private fun getSelectedTime(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return prefs.getString("pref_schedule_time", "08:00") ?: "08:00"
    }

    private fun getSelectedDays(): List<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return prefs.getStringSet("pref_schedule_days", setOf())?.toList() ?: listOf()
    }

    private fun saveScheduleToApi(time: String, days: List<String>) {
        val request = RemindersRequest(
            time = time,
            days = days.mapNotNull { dayCodeToInt(it) }
        )

        lifecycleScope.launch {
            try {
                apiService.addReminders(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dayCodeToInt(code: String): Int? = when (code.lowercase()) {
        "mon" -> 1
        "tue" -> 2
        "wed" -> 3
        "thu" -> 4
        "fri" -> 5
        "sat" -> 6
        "sun" -> 7
        else -> null
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is TimePickerPreference) {
            val dialog = TimePreferenceDialogFragment()
            dialog.setTargetFragment(this, 0)
            dialog.arguments = Bundle().apply {
                putString("key", preference.key)
            }
            dialog.show(parentFragmentManager, null)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }


}