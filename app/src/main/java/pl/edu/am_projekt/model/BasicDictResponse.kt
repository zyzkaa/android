package pl.edu.am_projekt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BasicDictResponse(
    val id: Int,
    val name: String
) : Parcelable
