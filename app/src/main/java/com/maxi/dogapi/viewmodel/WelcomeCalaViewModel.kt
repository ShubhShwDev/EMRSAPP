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
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WelcomeCalaViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<CalaResultResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<CalaResultResponse>> = _response
    private val _villageLiveData: MutableLiveData<String> = MutableLiveData()
    private val _villageLiveDatavillage_name: MutableLiveData<String> = MutableLiveData()
    val villageLiveData: MutableLiveData<String> =_villageLiveData
    val villageLiveDatavillage_name: MutableLiveData<String> =_villageLiveDatavillage_name
    val clickLocationTrackingEvent= MutableLiveData<Boolean>()


    val requestModel = LpRequestModel("5", "95524.73741",
    "583005")




fun startTrackingClass(){
    clickLocationTrackingEvent.postValue(true)

}

    fun clickHandleVillages(villageId:String,villageName:String){
        _villageLiveData.postValue("$villageId$$villageName")
//        _villageLiveData.value="$villageId$villageName"

}



/*
    fun fetchCalaVillageIDDetailsResponse(
        village_id: String,
        loginId: String,
        project_id: String,
        user_id: String,
        org_office_id: String
    ) = viewModelScope.launch {
        val requestModel = CalaRequestModel(utId, loginId,
            user_id,
            org_office_id)
        val jsonObject =  JSONObject()
        jsonObject.put("token","11552.11552")
//        jsonObject.put("uname",uName)
//        jsonObject.put("pwd",uPswd)
        jsonObject.put("ip_address","285244383")


        Log.e("valuJSON",requestModel.toString())
        repository.getCalaDetails(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }
    }
*/


    fun fetchCalaDetailsResponse(
        utId: String,
        loginId: String,
        user_id: String,
        org_office_id: String
    ) = viewModelScope.launch {
        val requestModel = CalaRequestModel(utId, loginId,
            user_id,
            org_office_id)
        val jsonObject =  JSONObject()
        jsonObject.put("token","11552.11552")
//        jsonObject.put("uname",uName)
//        jsonObject.put("pwd",uPswd)
        jsonObject.put("ip_address","285244383")


        Log.e("valuJSON",requestModel.toString())
        repository.getCalaDetails(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }
    }

}