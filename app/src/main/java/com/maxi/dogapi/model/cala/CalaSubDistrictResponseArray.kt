package com.maxi.dogapi.model.cala

import com.google.gson.annotations.SerializedName

data class CalaSubDistrictResponseArray (
    @SerializedName("sub_Disttrict_Name")
    val sub_Disttrict_Name: String,

    @SerializedName("sub_District_Id")
    val sub_District_Id: String,

    @SerializedName("calaVill")
    val calaVill: ArrayList<CalaVillageResponseArray>

)