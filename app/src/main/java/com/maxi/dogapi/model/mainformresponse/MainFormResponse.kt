package com.maxi.dogapi.model.mainformresponse

import com.google.gson.annotations.SerializedName

data class MainFormResponse(@SerializedName("status")
                            val status: Boolean = false,
@SerializedName("message")
val message: String = ""

)

