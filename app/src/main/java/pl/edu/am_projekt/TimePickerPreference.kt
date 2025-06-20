package pl.edu.am_projekt

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.preference.DialogPreference
import androidx.preference.PreferenceViewHolder

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

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val summaryView = holder.findViewById(android.R.id.summary) as? TextView
        summaryView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
    }
}