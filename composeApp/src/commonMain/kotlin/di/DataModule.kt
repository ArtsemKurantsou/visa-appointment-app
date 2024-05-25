package di

import com.russhwolf.settings.Settings
import data.AppDataRepositoryImpl
import domain.AppDataRepository
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val dataModule = module {
    single<Json> { Json { ignoreUnknownKeys = true; isLenient = true } }
    single<Settings>(qualifier<AppSettingsQualifier>()) { Settings() }
    single<AppDataRepository> {
        AppDataRepositoryImpl(
            get(qualifier<AppSettingsQualifier>()),
            get(),
        )
    }
}
