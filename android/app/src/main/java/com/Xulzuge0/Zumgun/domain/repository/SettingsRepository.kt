package com.Xulzuge0.Zumgun.domain.repository

import com.Xulzuge0.Zumgun.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>
    fun update(transform: (GameSettings) -> GameSettings)
}
