package com.maxi.dogapi.model.activityresponse

import com.google.gson.annotations.SerializedName

data class ActivityDetails(@SerializedName("activity_name")
                    val activity_name: String = "",
                           @SerializedName("tpqa_act_id")
                    val tpqa_act_id: Int = 0)