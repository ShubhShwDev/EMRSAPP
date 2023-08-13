package com.maxi.dogapi.data.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.maxi.dogapi.model.*
import com.maxi.dogapi.model.cala.CalaRequestModel
import com.maxi.dogapi.model.cala.CalaResultResponse
import com.maxi.dogapi.model.cala.CallaAssetsResponseArray
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.lp.LpResultResponse
import com.maxi.dogapi.model.mainformresponse.MainFormResponse
import com.maxi.dogapi.model.school.SchoolResponse
import com.maxi.dogapi.model.state.StateResponse
import com.maxi.dogapi.model.tpResponse.TpResponse
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyRequestModel
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyResJSONObject
import com.maxi.dogapi.utils.Constants
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import java.io.IOException

interface FeedService {

    @Headers("Accept: application/json")
    @POST(Constants.LOGIN_URL)
    suspend fun getLogin(@Body requestModel: RequestModel): Response<LoginResponseArray>


    @POST(Constants.LP_DETAILS_URL)
    suspend fun getLPDetails(
                       @Body requestModel: LpRequestModel
    ): Response<LpResultResponse>

//    @GET(Constants.CALA_DETAILSN_NPOINT_URL)
//    suspend fun getCalaDetails(
//    ): Response<CalaResultResponse>

    @POST(Constants.CALA_DETAILS_URL)
    suspend fun getCalaDetails(
                       @Body requestModel: CalaRequestModel
    ): Response<CalaResultResponse>

    @POST(Constants.SURVEY_VILLAGES_URL)
    suspend fun getCalaVillageSurveyDetails(
        @Body requestModel: CalaVillageSurveyRequestModel
    ): Response<CalaVillageSurveyResJSONObject>


    @POST(Constants.STATE_LIST_URL)
    suspend fun getStateList(@Body requestModel: StateRequestModel): Response<StateResponse>

    @POST(Constants.SCHOOL_URL)
    suspend fun getSchoolList(@Body requestModel: SchoolRequestModel): Response<SchoolResponse>

    @POST(Constants.TP_URL)
    suspend fun getTpList(@Body requestModel: TpRequestModel): Response<TpResponse>

    @POST(Constants.MAIN_FORM_URL)
    suspend fun getSubmitForm(@Body requestModel: MainFormRequestModel): Response<MainFormResponse>
}
