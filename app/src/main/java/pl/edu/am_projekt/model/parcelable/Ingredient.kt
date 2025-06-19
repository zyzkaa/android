package pl.edu.am_projekt.model.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(val productID : Int, val name: String, var quantity: Int) : Parcelable
