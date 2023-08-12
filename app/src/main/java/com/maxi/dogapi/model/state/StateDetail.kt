package com.maxi.dogapi.model.state

import com.google.gson.annotations.SerializedName

data class StateDetail(

@SerializedName("id")
val id: String,

@SerializedName("name")
val name: String
)