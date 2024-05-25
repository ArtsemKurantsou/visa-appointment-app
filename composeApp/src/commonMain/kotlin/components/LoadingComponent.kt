package components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import domain.GetPersonInteractor
import domain.Person
import kotlinx.coroutines.launch
import utils.componentScope

interface LoadingComponent

class DefaultLoadingComponent(
    componentContext: ComponentContext,
    private val getPersonInteractor: GetPersonInteractor,
    private val onLoaded: (person: Person?) -> Unit,
) : LoadingComponent, ComponentContext by componentContext {

    init {
        lifecycle.doOnCreate {
            loadStoredPerson()
        }
    }

    private fun loadStoredPerson() {
        componentScope.launch {
            onLoaded(getPersonInteractor())
        }
    }
}
