import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    SdkHolder.set(SomeSdkImpl())
    SdkHolder.process(Unit)
    App()
}