package data

import domain.Person
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val email: String,
    val phone: String,
)

fun Person.toDto(): PersonDto = PersonDto(
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    email = email,
    phone = phone,
)

fun PersonDto.toDomain(): Person = Person(
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    email = email,
    phone = phone,
)
