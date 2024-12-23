package com.isu.common.utils

import kotlinx.coroutines.flow.Flow

/**
 * Api use case
 * Generic interface for all api use case
 * @param I
 * @param R
 * @constructor Create empty Api use case
 */
interface ApiUseCase <I,R>{
    operator fun invoke(request: I): Flow<NetworkResource<R>>
}