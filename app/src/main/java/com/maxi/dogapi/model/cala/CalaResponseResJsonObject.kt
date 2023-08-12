package com.maxi.dogapi.model.cala

import com.google.gson.annotations.SerializedName

data class CalaResponseResJsonObject (
    @SerializedName("response_code")
    val response_code: String,

    @SerializedName("agency_id")
    val agency_id: String,

    @SerializedName("agency_name")
    val agency_name: String,

    @SerializedName("project_name")
    val project_name: String,

    @SerializedName("project_id")
    val project_id: String,

    @SerializedName("cala_name")
    val cala_name: String,

    @SerializedName("state_name")
    val state_name: String,

    @SerializedName("state_id")
    val state_id: String,

    @SerializedName("district_name")
    val district_name: String,

    @SerializedName("district_id")
    val district_id: String,

    @SerializedName("calaSubDistrict")
    val calaSubDistrict: ArrayList<CalaSubDistrictResponseArray>?

)