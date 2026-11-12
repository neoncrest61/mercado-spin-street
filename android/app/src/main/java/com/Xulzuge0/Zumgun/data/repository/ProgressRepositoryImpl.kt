package com.Xulzuge0.Zumgun.data.repository

import com.Xulzuge0.Zumgun.data.local.MercadoPreferences
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.RunResult
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressRepositoryImpl(
    private val preferences: MercadoPreferences
) : ProgressRepository {

    private val _progress = MutableStateFlow(preferences.readProgress())
    override val progress: StateFlow<BazaarProgress> = _progress.asStateFlow()

    private val _lastRun = MutableStateFlow<RunResult?>(null)
    override val lastRun: StateFlow<RunResult?> = _lastRun.asStateFlow()

    override fun update(transform: (BazaarProgress) -> BazaarProgress) {
        _progress.update(transform)
        preferences.writeProgress(_progress.value)
    }

    override fun publishRun(result: RunResult) {
        _lastRun.value = result
    }

    override fun resetBazaar() {
        preferences.clearProgress()
        _progress.value = preferences.readProgress()
        _lastRun.value = null
    }
}
