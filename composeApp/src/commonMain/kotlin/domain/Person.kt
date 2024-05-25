package domain

import kotlinx.datetime.LocalDate

data class Person(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val email: String,
    val phone: String,
)
