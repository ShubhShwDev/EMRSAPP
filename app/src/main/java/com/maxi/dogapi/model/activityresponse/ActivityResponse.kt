package com.maxi.dogapi.model.activityresponse

import com.google.gson.annotations.SerializedName

data class ActivityResponse(@SerializedName("data")
                         val data: ArrayList<ActivityDetails>?,
                            @SerializedName("status")
                         val status: Boolean = false)


