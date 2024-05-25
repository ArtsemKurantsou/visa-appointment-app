package components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import data.PersonDto
import data.toDomain
import data.toDto
import domain.Person
import kotlinx.serialization.Serializable

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    // Defines all possible child components
    sealed class Child {
        data class Loading(val component: LoadingComponent) : Child()
        data class PersonInput(val component: PersonInputComponent) : Child()
        data class Web(val component: WebComponent) : Child()
    }

    interface ComponentFactory {
        fun createLoadingComponent(
            componentContext: ComponentContext,
            onLoaded: (person: Person?) -> Unit,
        ): LoadingComponent

        fun createPersonInputComponent(
            componentContext: ComponentContext,
            person: Person?,
            onNext: (person: Person) -> Unit,
        ): PersonInputComponent

        fun createWebComponent(
            componentContext: ComponentContext,
        ): WebComponent
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: RootComponent.ComponentFactory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Loading,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Loading -> RootComponent.Child.Loading(loadingComponent(componentContext))
            is Config.PersonInput -> RootComponent.Child.PersonInput(
                personInputComponent(
                    componentContext,
                    config,
                )
            )

            is Config.Web -> RootComponent.Child.Web(webComponent(componentContext, config))
        }

    private fun loadingComponent(componentContext: ComponentContext): LoadingComponent {
        return componentFactory.createLoadingComponent(componentContext) { person ->
            navigation.replaceAll(Config.PersonInput(person?.toDto()))
        }
    }

    private fun personInputComponent(
        componentContext: ComponentContext,
        config: Config.PersonInput,
    ): PersonInputComponent {
        return componentFactory.createPersonInputComponent(
            componentContext,
            config.personDto?.toDomain(),
        ) {
            navigation.replaceAll(Config.Web)
        }
    }

    private fun webComponent(
        componentContext: ComponentContext,
        config: Config.Web,
    ): WebComponent {
        return componentFactory.createWebComponent(componentContext)
    }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Loading : Config

        @Serializable
        data class PersonInput(val personDto: PersonDto?) : Config

        @Serializable
        data object Web : Config
    }
}
