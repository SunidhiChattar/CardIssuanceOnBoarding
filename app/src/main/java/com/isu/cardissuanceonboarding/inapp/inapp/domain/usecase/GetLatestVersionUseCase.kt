package com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase

import com.isu.cardissuanceonboarding.inapp.inapp.common.NetworkResource
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.UserAppDetails
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.LatestVersionResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case class for handling biometric authentication submission.
 *
 * @param repository The repository providing data access and abstraction.
 */
class GetLatestVersionUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
        userAppDetails: UserAppDetails
    ): Flow<NetworkResource<LatestVersionResponseModel>> {
        return repository.getLatestAPKVersion(userAppDetails)
    }
}