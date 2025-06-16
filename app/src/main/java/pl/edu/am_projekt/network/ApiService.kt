package pl.edu.am_projekt.network

import MealResponseDAO
import okhttp3.MultipartBody
import pl.edu.am_projekt.model.BasicDictResponse
import pl.edu.am_projekt.model.ImageUploadResponse
import pl.edu.am_projekt.model.RegisterRequest
import pl.edu.am_projekt.model.workout.StrengthExerciseInfoResponse
import pl.edu.am_projekt.model.workout.WorkoutDetailsResponse
import pl.edu.am_projekt.model.workout.WorkoutShortResponse
import retrofit2.Call
import retrofit2.http.Part
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface ApiService {
    @POST("user/register")
    fun registerUser(@Body request: RegisterRequest): Call<Void>

    @POST("user/temp_login")
    fun tempLogin(): Call<Void>

//    @GET("exercise/strength")
//    fun getStrengthExercises() : Call<List<StrExerciseResponse>>

    @GET("training/{page}/{amount}")
    fun getTrainings(@Path("page") page: Int, @Path("amount") amount: Int) : Call<List<WorkoutShortResponse>>

    @GET("training/{id}")
    fun getTrainingDetails(@Path("id") id: Int) : Call<WorkoutDetailsResponse>

    @GET("exercise/muscles")
    suspend fun getAllMuscles() : List<BasicDictResponse>

    @GET("exercise/muscle/{id}")
    suspend fun getExercisesByMuscle(@Path("id") id: Int) : List<StrengthExerciseInfoResponse>

    @GET("exercise/cardio/search/{name}")
    suspend fun getCardioExercisesBySearch(@Path("name") name: String) : List<BasicDictResponse>

    @GET("exercise/strength/search/{name}")
    suspend fun getStrExercisesBySearch(@Path("name") name: String) : List<StrengthExerciseInfoResponse>

    @GET("exercise/strength/search/{name}/muscle/{id}")
    suspend fun getStrExercisesByMuscleAndSearch(@Path("name") name: String, @Path("id") id: Int) : List<StrengthExerciseInfoResponse>

    @GET("exercise/cardio")
    suspend fun getAllCardioExercises() : List<BasicDictResponse>

    @GET("exercise/strength/latest")
    suspend fun getRecentStrengthExercises() : List<StrengthExerciseInfoResponse>




    // Meals
    @GET("meal/get_all")
    suspend fun getAllMeals() : List<MealResponseDAO>

    @GET("meal/get_recent")
    suspend fun getRecentMeals(@Query("count") count: Int) : List<MealResponseDAO>

    @GET("meal/getMy")
    suspend fun getMyMeals() : List<MealResponseDAO>

    @Multipart
    @POST("api/image/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageUploadResponse>

}