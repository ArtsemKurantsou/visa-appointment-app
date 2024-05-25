package di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

val koinApplication: KoinApplication by lazy {
    initKoin()
}

private fun initKoin(): KoinApplication {
    return startKoin {
        modules(dataModule, domainModule, componentsModule)
    }
}
