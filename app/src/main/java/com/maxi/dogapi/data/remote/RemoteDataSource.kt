package com.maxi.dogapi.data.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxi.dogapi.model.RequestModel
import com.maxi.dogapi.model.SchoolRequestModel
import com.maxi.dogapi.model.StateRequestModel
import com.maxi.dogapi.model.cala.CalaRequestModel
import com.maxi.dogapi.model.cala.CallaAssetsResponseArray
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyRequestModel
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val feedService: FeedService) {

    suspend fun getLogin(requestModel:RequestModel) =
        feedService.getLogin(requestModel)

    suspend fun getLPDetails(requestModel: LpRequestModel) =
        feedService.getLPDetails(requestModel)

    suspend fun getCalaDetails(requestModel: CalaRequestModel) =
//        feedService.getCalaDetails()
        feedService.getCalaDetails(requestModel)

    suspend fun getCalaVillageSurveyDetails(requestModel: CalaVillageSurveyRequestModel) =
        feedService.getCalaVillageSurveyDetails(requestModel)

    suspend fun getState(stateRequestModel: StateRequestModel) =
        feedService.getStateList(stateRequestModel)

    suspend fun getSchoolList(stateRequestModel: SchoolRequestModel) =
        feedService.getSchoolList(stateRequestModel)


//    suspend fun getJsonDataFromAsset(context: Context) =
////        feedService.getCalaDetails()
//        feedService.getJsonDataFromAsset(context,"bezkoder.json")

}