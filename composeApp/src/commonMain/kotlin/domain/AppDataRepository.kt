package domain

import domain.Person

interface AppDataRepository {
    suspend fun getPerson(): Person?
    suspend fun savePerson(person: Person)
}
