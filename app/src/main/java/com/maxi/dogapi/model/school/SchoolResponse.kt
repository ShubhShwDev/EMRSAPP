package com.maxi.dogapi.model.school

import com.google.gson.annotations.SerializedName

data class SchoolResponse(@SerializedName("data")
                          val data: List<SchoolDetails>?,
                          @SerializedName("status")
                          val status: Boolean = false)