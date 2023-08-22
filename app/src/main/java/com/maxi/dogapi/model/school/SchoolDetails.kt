package com.maxi.dogapi.model.school

import com.google.gson.annotations.SerializedName

data class SchoolDetails(@SerializedName("name")
                    val name: String = "",
                         @SerializedName("id")
                    val id: Int = 0,
                         @SerializedName("state_id")
                    val stateId: Int = 0)