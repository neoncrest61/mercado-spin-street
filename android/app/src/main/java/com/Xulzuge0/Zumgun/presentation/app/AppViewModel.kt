package com.Xulzuge0.Zumgun.presentation.app

import androidx.lifecycle.ViewModel
import com.Xulzuge0.Zumgun.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NavState(
    val screen: Screen = Screen.Splash,
    val canGoBack: Boolean = false
)

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _state = MutableStateFlow(NavState())
    val state: StateFlow<NavState> = _state.asStateFlow()

    fun navigate(target: Screen) {
        val current = _state.value.screen
        if (current != Screen.Splash) backStack.add(current)
        publish(target)
    }

    fun swap(target: Screen) {
        publish(target)
    }

    fun openMenu() {
        backStack.clear()
        publish(Screen.Menu)
    }

    fun back() {
        if (backStack.isEmpty()) return
        val previous = backStack.removeAt(backStack.size - 1)
        publish(previous)
    }

    private fun publish(target: Screen) {
        _state.value = NavState(screen = target, canGoBack = backStack.isNotEmpty())
    }
}
