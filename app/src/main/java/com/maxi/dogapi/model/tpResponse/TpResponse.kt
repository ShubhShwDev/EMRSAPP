package com.maxi.dogapi.model.tpResponse

import com.google.gson.annotations.SerializedName

data class TpResponse(@SerializedName("data")
                         val data: ArrayList<TpDetails>?,
                      @SerializedName("status")
                         val status: Boolean = false)


