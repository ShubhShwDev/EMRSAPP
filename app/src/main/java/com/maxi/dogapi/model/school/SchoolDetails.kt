package com.maxi.dogapi.model.school

import com.google.gson.annotations.SerializedName

data class SchoolDetails(@SerializedName("name")
                    val name: String = "",
                         @SerializedName("id")
                    val id: Int = 0,
                         @SerializedName("block_id")
                    val blockId: Int = 0)