package pl.edu.am_projekt.model.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrient(val name: String, val amount: Double) : Parcelable
