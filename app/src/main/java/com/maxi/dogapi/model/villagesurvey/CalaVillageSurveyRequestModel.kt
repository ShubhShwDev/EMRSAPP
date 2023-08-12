package com.maxi.dogapi.model.villagesurvey

data class CalaVillageSurveyRequestModel(
    val village_id: String,
    val login_id: String,
    val project_id: String,
    val user_id: String,
    val org_office_id: String
    )