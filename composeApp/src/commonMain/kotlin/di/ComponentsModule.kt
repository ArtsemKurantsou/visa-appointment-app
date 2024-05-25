package di

import com.arkivanov.decompose.ComponentContext
import components.DefaultLoadingComponent
import components.DefaultPersonInputComponent
import components.DefaultRootComponent
import components.DefaultWebComponent
import components.LoadingComponent
import components.PersonInputComponent
import components.RootComponent
import components.WebComponent
import di.component.RootComponentFactoryImpl
import domain.Person
import org.koin.dsl.module

internal val componentsModule = module {
    factory<RootComponent> { (componentContext: ComponentContext) ->
        DefaultRootComponent(
            componentContext = componentContext,
            componentFactory = get(),
        )
    }
    factory<RootComponent.ComponentFactory> {
        RootComponentFactoryImpl(getKoin())
    }
    factory<LoadingComponent> { (
                                    componentContext: ComponentContext,
                                    onLoaded: (person: Person?) -> Unit,
                                ) ->
        DefaultLoadingComponent(
            componentContext = componentContext,
            getPersonInteractor = get(),
            onLoaded = onLoaded,
        )
    }
    factory<PersonInputComponent> { (
                                        componentContext: ComponentContext,
                                        person: Person?,
                                        onNext: (person: Person) -> Unit,
                                    ) ->
        DefaultPersonInputComponent(
            componentContext = componentContext,
            initialPerson = person,
            savePersonInteractor = get(),
            onNext = onNext,
        )
    }
    factory<WebComponent> { (componentContext: ComponentContext) ->
        DefaultWebComponent(componentContext)
    }
}