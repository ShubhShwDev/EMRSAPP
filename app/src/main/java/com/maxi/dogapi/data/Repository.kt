package com.maxi.dogapi.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxi.dogapi.data.remote.RemoteDataSource
import com.maxi.dogapi.model.*
import com.maxi.dogapi.model.cala.CalaRequestModel
import com.maxi.dogapi.model.cala.CalaResultResponse
import com.maxi.dogapi.model.cala.CallaAssetsResponseArray
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.lp.LpResultResponse
import com.maxi.dogapi.model.state.StateResponse
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyRequestModel
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyResJSONObject
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
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


    suspend fun getState(requestModel: StateRequestModel): Flow<NetworkResult<StateResponse>> {
        return flow<NetworkResult<StateResponse>> {
            emit(safeApiCall { remoteDataSource.getState(requestModel) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLPDetails(requestModel: LpRequestModel): Flow<NetworkResult<LpResultResponse>> {
        return flow<NetworkResult<LpResultResponse>> {
            emit(safeApiCall { remoteDataSource.getLPDetails(requestModel) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCalaDetails(requestModel: CalaRequestModel): Flow<NetworkResult<CalaResultResponse>> {
        return flow<NetworkResult<CalaResultResponse>> {
            emit(safeApiCall { remoteDataSource.getCalaDetails(requestModel) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getCalaVillageSurveyDetails(requestModel: CalaVillageSurveyRequestModel): Flow<NetworkResult<CalaVillageSurveyResJSONObject>> {
        return flow<NetworkResult<CalaVillageSurveyResJSONObject>> {
            emit(safeApiCall { remoteDataSource.getCalaVillageSurveyDetails(requestModel) })
        }.flowOn(Dispatchers.IO)
    }

/*
    fun getJsonDataFromAsset(context: Context, fileName: String): List<CallaAssetsResponseArray?> {
        val jsonString: String
        var persons: List<CallaAssetsResponseArray> = listOf()

        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val gson = Gson()
            val listPersonType = object : TypeToken<List<CallaAssetsResponseArray>>() {}.type

            persons = gson.fromJson(jsonString, listPersonType)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
        return persons
    }
*/

}
