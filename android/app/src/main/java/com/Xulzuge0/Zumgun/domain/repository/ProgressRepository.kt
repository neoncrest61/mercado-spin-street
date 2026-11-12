package com.Xulzuge0.Zumgun.domain.repository

import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.RunResult
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<BazaarProgress>
    val lastRun: StateFlow<RunResult?>
    fun update(transform: (BazaarProgress) -> BazaarProgress)
    fun publishRun(result: RunResult)
    fun resetBazaar()
}
