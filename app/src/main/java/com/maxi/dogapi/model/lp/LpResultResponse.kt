package com.maxi.dogapi.model.lp

import com.google.gson.annotations.SerializedName

data class LpResultResponse(
    @SerializedName("resp")
    val message: ArrayList<LpResponseArray>
)
