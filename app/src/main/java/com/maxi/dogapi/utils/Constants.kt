package com.maxi.dogapi.utils

class Constants {

    companion object {

        const val BASE_URL = "http://103.48.65.52:8080"
        const val LOGIN_URL = "$BASE_URL/EMRS/emrs_api/user/login.php"
        const val LP_DETAILS_URL = "$BASE_URL/laraz/api/revamp/GetLPdetails"
        const val CALA_DETAILS_URL = "$BASE_URL/laraz/api/revamp/GetCALAdetails"
        const val SURVEY_VILLAGES_URL = "$BASE_URL/laraz/api/revamp/GetSurveyVillages"
        const val STATE_URL = "$BASE_URL/EMRS/emrs_api/user/state.php"
        const val SCHOOL_URL = "$BASE_URL/EMRS/emrs_api/user/school.php"
//        const val SCHOOL_URL = "$BASE_URL/EMRS/emrs_api/user/school.php"
        const val RANDOM_URL_1= "api/breeds/image/random"
        const val STATE_LIST_URL= "$BASE_URL/EMRS/emrs_api/user/state.php"


    }
}

