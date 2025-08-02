package com.kurantsov.visaappointmentmaker

import App
import SdkHolder
import SomeSdk
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import components.RootComponent
import di.koinApplication
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SdkHolder.set(SomeSdkImpl())
        val rootComponent = koinApplication.koin.get<RootComponent> {
            parametersOf(defaultComponentContext())
        }
        setContent {
            App(rootComponent)
        }
        SdkHolder.process(intent)
    }
}

/*
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}*/
