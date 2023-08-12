package com.maxi.dogapi.model.cala

import com.google.gson.annotations.SerializedName

data class CallaAssetsResponseArray (
    @SerializedName("name")
    val name: String,

    @SerializedName("age")
    val age: String,

//    @SerializedName("calaVill")
//    val calaVill: ArrayList<CalaVillageResponseArray>

)