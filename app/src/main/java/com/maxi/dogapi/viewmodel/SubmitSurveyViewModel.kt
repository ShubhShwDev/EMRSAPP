package com.maxi.dogapi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxi.dogapi.data.Repository
import com.maxi.dogapi.data.remote.FeedService
import com.maxi.dogapi.model.*
import com.maxi.dogapi.model.cala.CallaAssetsResponseArray
import com.maxi.dogapi.model.school.SchoolDetails
import com.maxi.dogapi.model.school.SchoolResponse
import com.maxi.dogapi.model.state.StateDetail
import com.maxi.dogapi.model.tpResponse.TpDetails
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SubmitSurveyViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val _response: MutableLiveData<ArrayList<StateDetail>> = MutableLiveData()
     val _response_school: MutableLiveData<ArrayList<SchoolDetails>> = MutableLiveData()
     val _response_tp: MutableLiveData<ArrayList<TpDetails>> = MutableLiveData()
     val _response_main_form_submit: MutableLiveData<String> = MutableLiveData()
//    val stateList: LiveData<List<StateDetail>> = _response
//    val schoolList: LiveData<List<SchoolDetails>> = _response_school
//    val tpList: LiveData<List<TpDetails>> = _response_tp

    fun fetchStateResponse(userId: String, levelId: String, tpqaId: String) = viewModelScope.launch {
        val requestModel =  StateRequestModel(userId,levelId,tpqaId)
        Log.e("RESPONSESTATE","respnse"+userId+""+levelId+""+tpqaId)

        repository.getState(requestModel).collect { values ->
            _response.value?.clear()
            _response.postValue(values.data?.data)

//            stateList.value = values.data?.data
        }


    }

    fun fetchSchoolResponse(userId: String, levelId: String, tpqaId: String, stateId: String) = viewModelScope.launch {
        val requestModel =  SchoolRequestModel(userId,levelId,stateId,tpqaId)
        Log.e("RESPONSEschool","respnse"+requestModel+userId+""+levelId+""+tpqaId)
        repository.getSchoolList(requestModel).collect { values ->
            _response_school.value?.clear()
            _response_school.postValue(values.data?.data)
//            _response_school.value = values.data?.data
        }
    }

    fun fetchTpResponse(userId: String, levelId: String) = viewModelScope.launch {
        val requestModel =  TpRequestModel(userId,levelId)
        Log.e("RESPONSETp","respnse"+requestModel+userId+""+levelId+"")
        repository.getTpList(requestModel).collect { values ->
            _response_tp.value?.clear()
            _response_tp.postValue(values.data?.data)
//            _response_tp.value = values.data?.data
        }
    }

    fun fetchSubmitResponse(userId: String, levelId: String) = viewModelScope.launch {
//        val requestModel =  MainFormRequestModel(userId,levelId)
//        Log.e("RESPONSETp","respnse"+requestModel+userId+""+levelId+"")
//        repository.getSubmitApi(requestModel).collect { values ->
//            if (values.data?.status==true)
//            _response_main_form_submit.postValue(values.data?.message)
////            _response_tp.value = values.data?.data
//        }
    }

}