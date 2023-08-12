package com.maxi.dogapi.model.villagesurvey

import com.google.gson.annotations.SerializedName

data class CalaVillageSurveyResponseArray (


    @SerializedName("survey_id")
    val surveyId: String,

    @SerializedName("survey_no")
    val surveyName: String
)