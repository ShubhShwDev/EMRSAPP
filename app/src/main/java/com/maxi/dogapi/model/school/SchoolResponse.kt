package com.maxi.dogapi.model.school

import com.google.gson.annotations.SerializedName

data class SchoolResponse(@SerializedName("data")
                          val data: ArrayList<SchoolDetails>?,
                          @SerializedName("status")
                          val status: Boolean = false)