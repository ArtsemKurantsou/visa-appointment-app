package di.component

import com.arkivanov.decompose.ComponentContext
import components.LoadingComponent
import components.PersonInputComponent
import components.RootComponent
import components.WebComponent
import domain.Person
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

internal class RootComponentFactoryImpl(
    private val koin: Koin,
) : RootComponent.ComponentFactory {
    override fun createLoadingComponent(
        componentContext: ComponentContext,
        onLoaded: (person: Person?) -> Unit,
    ): LoadingComponent {
        return koin.get {
            parametersOf(componentContext, onLoaded)
        }
    }

    override fun createPersonInputComponent(
        componentContext: ComponentContext,
        person: Person?,
        onNext: (person: Person) -> Unit,
    ): PersonInputComponent {
        return koin.get {
            parametersOf(componentContext, person, onNext)
        }
    }

    override fun createWebComponent(componentContext: ComponentContext): WebComponent {
        return koin.get {
            parametersOf(componentContext)
        }
    }
}
