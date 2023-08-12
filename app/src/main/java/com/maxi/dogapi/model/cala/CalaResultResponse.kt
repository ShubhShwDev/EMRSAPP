package com.maxi.dogapi.model.cala

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CalaResultResponse(
//    @SerializedName("calaSubDistrict")
//    val calaSubDistrict: ArrayList<CalaSubDistrictResponseArray>?

    @SerializedName("resp")
    var respmsg: CalaResponseResJsonObject
)
