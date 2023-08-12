package com.maxi.dogapi.model.villagesurvey

import com.google.gson.annotations.SerializedName

data class CalaVillageSurveyResponse (

    @SerializedName("surveyVillList")
    val calaVill: ArrayList<CalaVillageSurveyResponseArray>

)