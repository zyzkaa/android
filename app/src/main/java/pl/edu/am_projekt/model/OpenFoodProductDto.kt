package pl.edu.am_projekt.model

import com.google.gson.annotations.SerializedName

data class OpenFoodProductDto(
    @SerializedName("product_name")
    val productName: String?,

    val nutriments: NutrientsDto?
)

data class NutrientsDto(
    @SerializedName("energy-kcal_100g")
    val energyKcal: Double?,

    @SerializedName("fat_100g")
    val fat: Double?,

    @SerializedName("carbohydrates_100g")
    val carbohydrates: Double?,

    @SerializedName("proteins_100g")
    val proteins: Double?
)
