package domain

class GetPersonInteractor internal constructor(
    private val repository: AppDataRepository,
) {
    suspend operator fun invoke(): Person? {
        return repository.getPerson()
    }
}
