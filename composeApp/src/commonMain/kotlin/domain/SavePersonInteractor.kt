package domain

class SavePersonInteractor internal constructor(
    private val repository: AppDataRepository,
) {
    suspend operator fun invoke(person: Person) {
        repository.savePerson(person)
    }
}
