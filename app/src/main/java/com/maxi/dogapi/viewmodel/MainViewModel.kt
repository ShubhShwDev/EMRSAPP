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
import com.maxi.dogapi.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<LoginResponseArray>> = MutableLiveData()
    val response: LiveData<NetworkResult<LoginResponseArray>> = _response

    fun fetchLoginResponse(uName: String, uPswd: String, uSeed: String) = viewModelScope.launch {
        val requestModel = RequestModel(uSeed, uName,
            uPswd,
            )
        val jsonObject =  JSONObject()
        jsonObject.put("token",uSeed)
//        jsonObject.put("uname",uName)
//        jsonObject.put("pwd",uPswd)
        jsonObject.put("username",uName)
        jsonObject.put("password",uPswd)

        Log.e("valuJSON",requestModel.toString())
        repository.getLogin(requestModel).collect { values ->
            _response.value = values
            Log.e("Values",response.value.toString()+ " "+values.toString())
        }


    }

}