package pl.edu.am_projekt;

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat
import java.util.*

class TimePreferenceDialogFragment : PreferenceDialogFragmentCompat(),
    TimePickerDialog.OnTimeSetListener {

    private var selectedHour = 8
    private var selectedMinute = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): TimePickerDialog {
        val pref = preference as TimePickerPreference
        val parts = pref.time.split(":").mapNotNull { it.toIntOrNull() }
        selectedHour = parts.getOrElse(0) { 8 }
        selectedMinute = parts.getOrElse(1) { 0 }

        return TimePickerDialog(requireContext(), this, selectedHour, selectedMinute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val formatted = "%02d:%02d".format(hourOfDay, minute)
        val pref = preference
        if (pref is DialogPreference && pref.callChangeListener(formatted)) {
            (pref as? TimePickerPreference)?.persistTime(formatted)
        }

    }

    override fun onDialogClosed(positiveResult: Boolean) {

    }
}


