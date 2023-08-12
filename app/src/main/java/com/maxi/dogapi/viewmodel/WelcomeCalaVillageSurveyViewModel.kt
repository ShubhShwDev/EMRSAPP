package com.maxi.dogapi.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxi.dogapi.data.Repository
import com.maxi.dogapi.model.RequestModel
import com.maxi.dogapi.model.cala.CalaRequestModel
import com.maxi.dogapi.model.cala.CalaResultResponse
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.lp.LpResultResponse
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyRequestModel
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyResJSONObject
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WelcomeCalaVillageSurveyViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<CalaVillageSurveyResJSONObject>> = MutableLiveData()
    val response: LiveData<NetworkResult<CalaVillageSurveyResJSONObject>> = _response





    fun fetchCalaDetailsResponse(
        village_id: String,
        loginId: String,
        project_id: String,
        user_id: String,
        org_office_id: String
    ) = viewModelScope.launch {
        val requestModel = CalaVillageSurveyRequestModel(village_id, loginId,
            project_id,user_id,
            org_office_id)
        Log.e("valuJSON",requestModel.toString())
        repository.getCalaVillageSurveyDetails(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }
    }

}