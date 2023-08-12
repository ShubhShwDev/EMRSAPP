package com.maxi.dogapi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxi.dogapi.data.Repository
import com.maxi.dogapi.model.LoginResponseArray
import com.maxi.dogapi.model.LoginResultResponse
import com.maxi.dogapi.model.RequestModel
import com.maxi.dogapi.model.cala.CallaAssetsResponseArray
import com.maxi.dogapi.model.school.SchoolDetails
import com.maxi.dogapi.model.state.StateDetail
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

    var stateList = MutableLiveData<List<StateDetail>>()
    var schoolList = MutableLiveData<List<SchoolDetails>>()

    fun fetchStateResponse(userId: String, levelId: String, tpqaId: String) = viewModelScope.launch {

        val jsonObject =  JSONObject()
        jsonObject.put("user_id",userId)
        jsonObject.put("level_id",levelId)
        jsonObject.put("tpqa_id",tpqaId)

/*
        repository.getLogin(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }
*/


    }

}