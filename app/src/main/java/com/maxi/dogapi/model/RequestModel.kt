package com.maxi.dogapi.model

data class RequestModel(
    val token: String,
    val username: String,
    val password: String
//    val ip_address: String
)