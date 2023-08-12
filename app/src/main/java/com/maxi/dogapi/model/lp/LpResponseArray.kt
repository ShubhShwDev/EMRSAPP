package com.maxi.dogapi.model.lp

import com.google.gson.annotations.SerializedName

data class LpResponseArray (
    @SerializedName("project_name")
    val project_name: String,

    @SerializedName("project_id")
    val project_id: String,

    @SerializedName("party_id")
    val party_id: String,

    @SerializedName("survey_id")
    val survey_id: String,

    @SerializedName("survey_no")
    val survey_no: String,

    @SerializedName("survey_no_hin")
    val survey_no_hin: String,

    @SerializedName("area")
    val area: String,

    @SerializedName("total_area")
    val total_area: String,

    @SerializedName("cala_name")
    val cala_name: String,

    @SerializedName("state_name")
    val state_name: String,

    @SerializedName("district_name")
    val district_name: String,

    @SerializedName("sub_district_name")
    val sub_district_name: String,

    @SerializedName("village_name")
    val village_name: String,

    @SerializedName("village_id")
    val village_id: String,

    @SerializedName("laNotification")
    val laNotification: String,

    @SerializedName("threeANotification")
    val threeANotification: String,

    @SerializedName("threeDNotification")
    val threeDNotification: String,

    @SerializedName("compensationAmt")
    val compensationAmt: String
)