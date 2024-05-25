package components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import domain.Person
import domain.SavePersonInteractor
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import utils.componentScope

interface PersonInputComponent {
    val state: Value<State>

    fun onFirstNameChanged(firstName: String)
    fun onLastNameChanged(lastName: String)
    fun onBirthDateChanged(birthDate: LocalDate)
    fun onEmailChanged(email: String)
    fun onPhoneChanged(phone: String)
    fun onNext()

    data class State(
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate,
        val email: String,
        val phone: String,
    )
}

class DefaultPersonInputComponent(
    componentContext: ComponentContext,
    initialPerson: Person?,
    private val savePersonInteractor: SavePersonInteractor,
    private val onNext: (person: Person) -> Unit,
) : PersonInputComponent, ComponentContext by componentContext {

    override val state: MutableValue<PersonInputComponent.State> =
        MutableValue(
            initialPerson?.toState() ?: INITIAL_STATE,
        )

    override fun onFirstNameChanged(firstName: String) {
        state.update {
            it.copy(firstName = firstName)
        }
    }

    override fun onLastNameChanged(lastName: String) {
        state.update {
            it.copy(lastName = lastName)
        }
    }

    override fun onBirthDateChanged(birthDate: LocalDate) {
        state.update {
            it.copy(birthDate = birthDate)
        }
    }

    override fun onEmailChanged(email: String) {
        state.update {
            it.copy(email = email)
        }
    }

    override fun onPhoneChanged(phone: String) {
        state.update {
            it.copy(phone = phone)
        }
    }

    override fun onNext() {
        componentScope.launch {
            val person = state.value.toPerson()
            savePersonInteractor(person)
            onNext(person)
        }
    }

    private fun Person.toState() = PersonInputComponent.State(
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        email = email,
        phone = phone,
    )

    private fun PersonInputComponent.State.toPerson() = Person(
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        email = email,
        phone = phone,
    )

    private companion object {
        val INITIAL_STATE: PersonInputComponent.State
            get() = PersonInputComponent.State(
                firstName = "",
                lastName = "",
                birthDate = LocalDate(2000, 2, 2),
                phone = "",
                email = "",
            )
    }
}
