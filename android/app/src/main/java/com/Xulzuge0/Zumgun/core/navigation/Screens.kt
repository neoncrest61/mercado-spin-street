package com.Xulzuge0.Zumgun.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelSelect : Screen
    data class Game(val stallId: Int) : Screen
    data object Result : Screen
    data object Collection : Screen
    data object Progress : Screen
    data object Settings : Screen
}
