package pl.edu.am_projekt.network


import MealResponseDAO
import okhttp3.MultipartBody
import pl.edu.am_projekt.model.BasicDictResponse
import pl.edu.am_projekt.model.CreateMealDto
import pl.edu.am_projekt.model.CreateMealPlanDto
import pl.edu.am_projekt.model.ImageUploadResponse
import pl.edu.am_projekt.model.LoginRequest
import pl.edu.am_projekt.model.NutrientsInput
import pl.edu.am_projekt.model.RegisterRequest
import pl.edu.am_projekt.model.ServerResponse
import pl.edu.am_projekt.model.UpdatedNutrientsReturn
import pl.edu.am_projekt.model.workout.request.RemindersRequest
import pl.edu.am_projekt.model.workout.request.WorkoutRequest
import pl.edu.am_projekt.model.workout.response.BasicExerciseResponse
import pl.edu.am_projekt.model.workout.response.StrengthExerciseInfoResponse
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.model.workout.response.WorkoutShortResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("user/temp_login")
    fun tempLogin(): Call<Void>

    @POST("user/login")
    suspend fun login(@Body userLoginRequest : LoginRequest)

    @POST("user/logout")
    suspend fun logout()

//    @GET("exercise/strength")
//    fun getStrengthExercises() : Call<List<StrExerciseResponse>>

    @POST("user/fcm/{token}")
    suspend fun setFcmToken(@Path("token") token: String)

    @POST("user/register")
    suspend fun registerUser(@Body userRegisterRequest: RegisterRequest)

    @POST("user/reminders")
    suspend fun addReminders(@Body request: RemindersRequest)

    @GET("training/{page}/{amount}")
    suspend fun getTrainings(@Path("page") page: Int, @Path("amount") amount: Int) : MutableList<WorkoutShortResponse>

    @GET("training/{id}")
    suspend fun getTrainingDetails(@Path("id") id: Int) : WorkoutDetailsResponse

    @GET("exercise/muscles")
    suspend fun getAllMuscles() : List<BasicDictResponse>

    @GET("exercise/muscle/{id}")
    suspend fun getExercisesByMuscle(@Path("id") id: Int) : List<StrengthExerciseInfoResponse>

    @GET("exercise/cardio/search/{name}")
    suspend fun getCardioExercisesBySearch(@Path("name") name: String) : List<BasicExerciseResponse>

    @GET("exercise/strength/search/{name}")
    suspend fun getStrExercisesBySearch(@Path("name") name: String) : List<StrengthExerciseInfoResponse>

    @GET("exercise/strength/search/{name}/muscle/{id}")
    suspend fun getStrExercisesByMuscleAndSearch(@Path("name") name: String, @Path("id") id: Int) : List<StrengthExerciseInfoResponse>

    @GET("exercise/cardio")
    suspend fun getAllCardioExercises() : List<BasicExerciseResponse>

    @GET("exercise/strength/latest")
    suspend fun getRecentStrengthExercises() : List<StrengthExerciseInfoResponse>

    @POST("training")
    suspend fun postWorkout(@Body workout : WorkoutRequest) : WorkoutDetailsResponse

    @DELETE("training/{id}")
    suspend fun deleteWorkout(@Path("id") id: Int)




    // Meals
    @GET("meal/get_all")
    suspend fun getAllMeals() : List<MealResponseDAO>

    @GET("meal/get_recent")
    suspend fun getRecentMeals(@Query("count") count: Int) : List<MealResponseDAO>

    @GET("meal/getMy")
    suspend fun getMyMeals() : List<MealResponseDAO>

    @GET("meal/getMyEdited")
    suspend fun getMyEditedMeals() : List<MealResponseDAO>

    @POST("meal/add")
    suspend fun addNewMeal(@Body meal : CreateMealDto)

    @POST("meal_plan/add")
    suspend fun addMealPlan(@Body mealPlan: CreateMealPlanDto) : ServerResponse


    @Multipart
    @POST("api/image/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageUploadResponse>

    @POST("meal/update_calories_and_nutrients")
    suspend fun updateCaloriesAndNutrients(@Body ingredients: List<NutrientsInput>): Response<UpdatedNutrientsReturn>


}