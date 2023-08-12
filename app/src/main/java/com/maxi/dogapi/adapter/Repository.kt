package com.maxi.dogapi.adapter

import com.maxi.dogapi.data.remote.RemoteDataSource
import com.maxi.dogapi.model.BaseApiResponse
import com.maxi.dogapi.model.LoginResponseArray
import com.maxi.dogapi.model.LoginResultResponse
import com.maxi.dogapi.model.RequestModel
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.lp.LpResultResponse
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getLogin(requestModel: RequestModel): Flow<NetworkResult<LoginResponseArray>> {
        return flow<NetworkResult<LoginResponseArray>> {
            emit(safeApiCall { remoteDataSource.getLogin(requestModel) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLPDetails(requestModel: LpRequestModel): Flow<NetworkResult<LpResultResponse>> {
        return flow<NetworkResult<LpResultResponse>> {
            emit(safeApiCall { remoteDataSource.getLPDetails(requestModel) })
        }.flowOn(Dispatchers.IO)
    }

}
