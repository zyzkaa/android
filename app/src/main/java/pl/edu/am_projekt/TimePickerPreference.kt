package pl.edu.am_projekt

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference

class TimePickerPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : DialogPreference(context, attrs) {

    var time: String = "08:00"

    fun persistTime(value: String) {
        time = value
        persistString(value)
        summary = value
        notifyChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        val value = getPersistedString(defaultValue as? String ?: "08:00")
        persistTime(value)
    }
}