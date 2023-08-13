package com.maxi.dogapi.model.state

import com.google.gson.annotations.SerializedName

data class StateResponse(@SerializedName("data")
                         val data: ArrayList<StateDetail>?,
                         @SerializedName("status")
                         val status: Boolean = false)