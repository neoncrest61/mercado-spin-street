package com.Xulzuge0.Zumgun.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Xulzuge0.Zumgun.presentation.app.AppViewModel
import com.Xulzuge0.Zumgun.presentation.collection.CollectionViewModel
import com.Xulzuge0.Zumgun.presentation.game.GameViewModel
import com.Xulzuge0.Zumgun.presentation.levelselect.LevelSelectViewModel
import com.Xulzuge0.Zumgun.presentation.menu.MenuViewModel
import com.Xulzuge0.Zumgun.presentation.progress.ProgressViewModel
import com.Xulzuge0.Zumgun.presentation.result.ResultViewModel
import com.Xulzuge0.Zumgun.presentation.settings.SettingsViewModel
import com.Xulzuge0.Zumgun.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val progress = ServiceLocator.progressRepository
        val settings = ServiceLocator.settingsRepository
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(progress, settings)
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) -> LevelSelectViewModel(progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                progress,
                settings,
                ServiceLocator.spinReels,
                ServiceLocator.evaluatePaylines,
                ServiceLocator.applyRunResult
            )
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> ResultViewModel(progress, settings)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> CollectionViewModel(progress)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(progress)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settings, progress)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
