package com.maxi.dogapi.model

import com.google.gson.annotations.SerializedName

data class LoginResultResponse(
    @SerializedName("resp")
    val message: List<LoginResponseArray>
)
