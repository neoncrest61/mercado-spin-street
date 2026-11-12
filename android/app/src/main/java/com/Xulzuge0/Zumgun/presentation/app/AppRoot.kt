package com.Xulzuge0.Zumgun.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.navigation.Screen
import com.Xulzuge0.Zumgun.presentation.collection.CollectionScreen
import com.Xulzuge0.Zumgun.presentation.game.GameScreen
import com.Xulzuge0.Zumgun.presentation.levelselect.LevelSelectScreen
import com.Xulzuge0.Zumgun.presentation.menu.MenuScreen
import com.Xulzuge0.Zumgun.presentation.progress.ProgressScreen
import com.Xulzuge0.Zumgun.presentation.result.ResultScreen
import com.Xulzuge0.Zumgun.presentation.settings.SettingsScreen
import com.Xulzuge0.Zumgun.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val nav by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = nav.canGoBack && nav.screen !is Screen.Game) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = nav.screen,
        transitionSpec = {
            (fadeIn(tween(380)) + slideInVertically { height -> height / 12 }) togetherWith
                fadeOut(tween(380))
        },
        label = "screenTransition"
    ) { screen ->
        when (screen) {
            Screen.Splash -> SplashScreen(
                onFinished = { appViewModel.swap(Screen.Menu) }
            )

            Screen.Menu -> MenuScreen(
                onPlay = { stallId -> appViewModel.navigate(Screen.Game(stallId)) },
                onLevels = { appViewModel.navigate(Screen.LevelSelect) },
                onCollection = { appViewModel.navigate(Screen.Collection) },
                onProgress = { appViewModel.navigate(Screen.Progress) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )

            Screen.LevelSelect -> LevelSelectScreen(
                onBack = { appViewModel.back() },
                onStart = { stallId -> appViewModel.navigate(Screen.Game(stallId)) }
            )

            is Screen.Game -> GameScreen(
                stallId = screen.stallId,
                onRunFinished = { appViewModel.swap(Screen.Result) },
                onMenu = { appViewModel.openMenu() }
            )

            Screen.Result -> ResultScreen(
                onSpinAgain = { stallId -> appViewModel.swap(Screen.Game(stallId)) },
                onMenu = { appViewModel.openMenu() },
                onCollection = { appViewModel.navigate(Screen.Collection) }
            )

            Screen.Collection -> CollectionScreen(
                onBack = { appViewModel.back() },
                onLevels = { appViewModel.swap(Screen.LevelSelect) }
            )

            Screen.Progress -> ProgressScreen(
                onBack = { appViewModel.back() },
                onLevels = { appViewModel.swap(Screen.LevelSelect) }
            )

            Screen.Settings -> SettingsScreen(
                onBack = { appViewModel.back() }
            )
        }
    }
}
