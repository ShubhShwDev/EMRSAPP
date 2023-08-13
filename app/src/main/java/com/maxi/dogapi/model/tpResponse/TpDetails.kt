package com.maxi.dogapi.model.tpResponse

import com.google.gson.annotations.SerializedName

data class TpDetails(@SerializedName("tpqa_name")
                    val name: String = "",
                     @SerializedName("tpqa_id")
                    val tpqaId: Int = 0)