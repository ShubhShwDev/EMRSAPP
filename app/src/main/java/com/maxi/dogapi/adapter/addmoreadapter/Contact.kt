package com.example.smarttag.test

import com.maxi.dogapi.model.activityresponse.ActivityDetails

data class Contact(var observation: String?, var image: String?, var selectactivity: String?, var activityList:ArrayList<ActivityDetails>?,
     var spinnerActivityPosition: Int?=-1)