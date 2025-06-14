package pl.edu.am_projekt.network

import pl.edu.am_projekt.model.BasicDictResponse
import pl.edu.am_projekt.model.RegisterRequest
import pl.edu.am_projekt.model.workout.request.WorkoutRequest
import pl.edu.am_projekt.model.workout.response.StrengthExerciseInfoResponse
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.model.workout.response.WorkoutShortResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @POST("user/register")
    fun registerUser(@Body request: RegisterRequest): Call<Void>

    @POST("user/temp_login")
    fun tempLogin(): Call<Void>

//    @GET("exercise/strength")
//    fun getStrengthExercises() : Call<List<StrExerciseResponse>>

    @GET("training/{page}/{amount}")
    suspend fun getTrainings(@Path("page") page: Int, @Path("amount") amount: Int) : MutableList<WorkoutShortResponse>

    @GET("training/{id}")
    suspend fun getTrainingDetails(@Path("id") id: Int) : WorkoutDetailsResponse

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

    @POST("training")
    suspend fun postWorkout(@Body workout : WorkoutRequest) : WorkoutDetailsResponse

    @DELETE("training/{id}")
    suspend fun deleteWorkout(@Path("id") id: Int)
}