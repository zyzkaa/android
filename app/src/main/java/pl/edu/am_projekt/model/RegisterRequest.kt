package pl.edu.am_projekt.model

data class RegisterRequest(
    val username: String,
    val password: String,
    val weight: Int,
    val height: Int,
    val age: Int,
    val calories: Int
)