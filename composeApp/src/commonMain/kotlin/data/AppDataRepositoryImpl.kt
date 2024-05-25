package data

import com.russhwolf.settings.Settings
import domain.AppDataRepository
import domain.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class AppDataRepositoryImpl(
    private val settings: Settings,
    private val json: Json,
) : AppDataRepository {

    override suspend fun getPerson(): Person? = withContext(Dispatchers.IO) {
        return@withContext settings.getStringOrNull(PERSON_SETTING)?.let {
            json.decodeFromString<PersonDto>(it).toDomain()
        }
    }

    override suspend fun savePerson(person: Person) = withContext(Dispatchers.IO) {
        settings.putString(
            PERSON_SETTING,
            json.encodeToString(PersonDto.serializer(), person.toDto()),
        )
    }

    private companion object {
        val PERSON_SETTING = "person"
    }
}
