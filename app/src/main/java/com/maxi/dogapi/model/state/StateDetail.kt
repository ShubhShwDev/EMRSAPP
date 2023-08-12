package com.maxi.dogapi.model.state

import com.google.gson.annotations.SerializedName

data class StateDetail(@SerializedName("name")
                    val name: String = "",
                       @SerializedName("id")
                    val id: Int = 0)