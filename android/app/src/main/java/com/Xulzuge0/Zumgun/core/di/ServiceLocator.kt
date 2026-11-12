package com.Xulzuge0.Zumgun.core.di

import android.content.Context
import com.Xulzuge0.Zumgun.data.local.MercadoPreferences
import com.Xulzuge0.Zumgun.data.repository.ProgressRepositoryImpl
import com.Xulzuge0.Zumgun.data.repository.SettingsRepositoryImpl
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import com.Xulzuge0.Zumgun.domain.usecase.ApplyRunResultUseCase
import com.Xulzuge0.Zumgun.domain.usecase.EvaluatePaylinesUseCase
import com.Xulzuge0.Zumgun.domain.usecase.SpinReelsUseCase

object ServiceLocator {

    private var progressRepo: ProgressRepository? = null
    private var settingsRepo: SettingsRepository? = null

    val spinReels: SpinReelsUseCase = SpinReelsUseCase()
    val evaluatePaylines: EvaluatePaylinesUseCase = EvaluatePaylinesUseCase()
    val applyRunResult: ApplyRunResultUseCase = ApplyRunResultUseCase()

    fun init(context: Context) {
        if (progressRepo != null && settingsRepo != null) return
        val preferences = MercadoPreferences(context.applicationContext)
        progressRepo = ProgressRepositoryImpl(preferences)
        settingsRepo = SettingsRepositoryImpl(preferences)
    }

    val progressRepository: ProgressRepository
        get() = requireNotNull(progressRepo)

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settingsRepo)
}
