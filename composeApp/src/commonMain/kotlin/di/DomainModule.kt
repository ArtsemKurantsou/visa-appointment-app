package di

import domain.GetPersonInteractor
import domain.SavePersonInteractor
import org.koin.dsl.module

internal val domainModule = module {
    factory { GetPersonInteractor(get()) }
    factory { SavePersonInteractor(get()) }
}