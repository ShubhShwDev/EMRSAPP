package com.maxi.dogapi.model.cala

import com.google.gson.annotations.SerializedName

data class CalaVillageResponseArray (


    @SerializedName("village_id")
    val village_id: String,

    @SerializedName("village_name")
    val village_name: String
)