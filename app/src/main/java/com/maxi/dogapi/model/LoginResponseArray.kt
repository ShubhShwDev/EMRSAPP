package com.maxi.dogapi.model

import com.google.gson.annotations.SerializedName

data class LoginResponseArray (
//    @SerializedName("response_code")
//    val response_code: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("level_id")
    val level_id: String,

    @SerializedName("tpqa_id")
    val tpqa_id: String,

    @SerializedName("full_name")
    val full_name: String
//
//    @SerializedName("agency_id")
//    val agency_id: String,
//
//    @SerializedName("project_id")
//    val project_id: String,
//
//    @SerializedName("user_id")
//    val user_id: String,
//
//    @SerializedName("ep_id")
//    val ep_id: String,
//
//    @SerializedName("otype")
//    val otype: String,
//
//    @SerializedName("utid")
//    val utid: String,
//
//    @SerializedName("loginid")
//    val loginId: String,
//
//    @SerializedName("ot_id")
//    val ot_id: String,
//
//    @SerializedName("org_office_id")
//    val org_office_id: String,
//
//    @SerializedName("ce_la")
//    val ce_la: String,
//
//    @SerializedName("name")
//    val name: String,
)