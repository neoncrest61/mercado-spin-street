package com.Xulzuge0.Zumgun.presentation.settings

import com.Xulzuge0.Zumgun.domain.model.ReelSpeed
import com.Xulzuge0.Zumgun.domain.model.StartingStall

data class SettingsUiState(
    val reelSpeed: ReelSpeed = ReelSpeed.NORMAL,
    val payLineHighlight: Boolean = true,
    val fullCelebration: Boolean = true,
    val startingStall: StartingStall = StartingStall.LAST_PLAYED
)
