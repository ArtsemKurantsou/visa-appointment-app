package utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

private data object COROUTINE_SCOPE_KEY

val ComponentContext.componentScope: CoroutineScope
    get() {
        return instanceKeeper.getOrCreate(COROUTINE_SCOPE_KEY) {
            ComponentCoroutineScope(MainScope()).also { scope ->
                lifecycle.doOnDestroy {
                    scope.onDestroy()
                    instanceKeeper.remove(COROUTINE_SCOPE_KEY)
                }
            }
        }
    }

private class ComponentCoroutineScope(
    scope: CoroutineScope
) : CoroutineScope by scope, InstanceKeeper.Instance {
    override fun onDestroy() {
        cancel()
    }
}