package pl.edu.am_projekt.model.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(val name: String, val quantity: Double) : Parcelable
