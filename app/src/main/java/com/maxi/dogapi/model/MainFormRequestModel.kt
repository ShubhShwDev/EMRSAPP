package com.maxi.dogapi.model

data class MainFormRequestModel(
    val user_id: String,
    val level_id: String,
    val tpqa_id: String,
    val officer_name: String,
    val designation: String,
    val contact: String,
    val email: String,
    val state_id: String,
    val school_id: String,
    val visit_date: String,
    val visit_time: String,
    val lat: String,
    val long: String,
    val activity_id: ArrayList<String>,
    val observation: ArrayList<String>,
    val photo: ArrayList<String>,
    val int_plumb: String,
    val int_elec_work: String,
    val ext_service: String,
    val oth_dev_work: String,
    val mat_qual: String,
    val over_observation: String,
    val remarks: String
)