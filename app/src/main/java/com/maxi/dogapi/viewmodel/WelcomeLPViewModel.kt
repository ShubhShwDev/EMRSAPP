package com.maxi.dogapi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxi.dogapi.data.Repository
import com.maxi.dogapi.model.lp.LpRequestModel
import com.maxi.dogapi.model.lp.LpResultResponse
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WelcomeLPViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<LpResultResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<LpResultResponse>> = _response
    val clickLocationTrackingEvent= MutableLiveData<Boolean>()







fun startTrackingClass(){
    clickLocationTrackingEvent.postValue(true)

}



    fun fetchLpDetailsResponse(utId: String, loginId: String, user_id: String) = viewModelScope.launch {
        val requestModel = LpRequestModel(utId, loginId,
            user_id)
        val jsonObject =  JSONObject()
        jsonObject.put("token","11552.11552")
//        jsonObject.put("uname",uName)
//        jsonObject.put("pwd",uPswd)
        jsonObject.put("ip_address","285244383")

        Log.e("valuJSON",jsonObject.toString())
        repository.getLPDetails(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }
    }

}