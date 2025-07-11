package pl.edu.am_projekt.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.edu.am_projekt.MyCookieJar
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .cookieJar(MyCookieJar())
        .addInterceptor(logging)
        .build()

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
//            .baseUrl("http://192.168.1.23:5010/")
            .baseUrl("http://192.168.1.22:5010/")
//            .baseUrl("http://10.0.2.2:5010/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}