import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.edu.am_projekt.model.IngredientReturnDto

data class MealResponseDAO(
    val id: Int,
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val creatorID: Int,
    val createdAt: String,
    val imageURL: String,
    val description: String?,
    val originalMealID: Int?,
    val ingredients: List<IngredientReturnDto>
)
