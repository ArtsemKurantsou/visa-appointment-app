package components

import com.arkivanov.decompose.ComponentContext

interface WebComponent {

}

internal class DefaultWebComponent(
    componentContext: ComponentContext,
) : WebComponent, ComponentContext by componentContext {

}
