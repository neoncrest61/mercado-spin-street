package com.Xulzuge0.Zumgun.data.repository

import com.Xulzuge0.Zumgun.data.local.MercadoPreferences
import com.Xulzuge0.Zumgun.domain.model.GameSettings
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryImpl(
    private val preferences: MercadoPreferences
) : SettingsRepository {

    private val _settings = MutableStateFlow(preferences.readSettings())
    override val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        _settings.update(transform)
        preferences.writeSettings(_settings.value)
    }
}
