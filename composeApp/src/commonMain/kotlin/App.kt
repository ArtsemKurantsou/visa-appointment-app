import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.ui.progress.AdaptiveCircularProgressIndicator
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import components.LoadingComponent
import components.PersonInputComponent
import components.RootComponent
import components.WebComponent
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun App(
    component: RootComponent,
) {
    MaterialTheme {
        Surface(Modifier.fillMaxWidth()) {
            RootContent(component)
        }
    }
}

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.stack,
        modifier = modifier.fillMaxSize(),
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Loading -> LoadingContent(component = child.component)
            is RootComponent.Child.PersonInput -> PersonInputContent(component = child.component)
            is RootComponent.Child.Web -> WebContent(component = child.component)
        }
    }
}

@Composable
fun LoadingContent(
    component: LoadingComponent,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        AdaptiveCircularProgressIndicator(modifier = Modifier.size(72.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonInputContent(
    component: PersonInputComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    var dateSelectorOpened: Boolean by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {
        Text(
            text = "Person input",
            style = MaterialTheme.typography.headlineMedium,
        )
        TextField(
            value = state.firstName,
            onValueChange = component::onFirstNameChanged,
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = state.lastName,
            onValueChange = component::onLastNameChanged,
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = state.birthDate.toString(),
            readOnly = true,
            onValueChange = { },
            label = { Text("Date of birth") },
            modifier = Modifier.fillMaxWidth().onFocusEvent {
                    if (!dateSelectorOpened && it.isFocused) {
                        dateSelectorOpened = true
                    }
                },
        )
        TextField(
            value = state.email,
            onValueChange = component::onEmailChanged,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = state.phone,
            onValueChange = component::onPhoneChanged,
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = component::onNext,
            modifier = Modifier.width(120.dp),
        ) {
            Text(text = "Next")
        }

        if (dateSelectorOpened) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.birthDate.toEpochDays().days.inWholeMilliseconds,
            )

            DatePickerDialog(onDismissRequest = { dateSelectorOpened = false }, confirmButton = {
                Button(onClick = {
                    dateSelectorOpened = false
                    component.onBirthDateChanged(
                        LocalDate.fromEpochDays(
                            datePickerState.selectedDateMillis!!.milliseconds.inWholeDays.toInt(),
                        )
                    )
                }) {
                    Text("Confirm")
                }
            }, dismissButton = {
                Button(onClick = {
                    dateSelectorOpened = false
                }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun WebContent(
    component: WebComponent,
    modifier: Modifier = Modifier,
) {
    val state = rememberWebViewState(
        url = "https://consulat.gouv.fr/en/ambassade-de-france-en-irlande/appointment",
    ).also {
        it.webSettings.isJavaScriptEnabled = true
        it.webSettings.logSeverity = KLogSeverity.Verbose
    }
    WebView(state, modifier)
}