import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    SdkHolder.set(SomeSdkImpl())
    App()
}