package com.maxi.dogapi.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxi.dogapi.R
import com.maxi.dogapi.adapter.villaggesurveyadapter.CalaVillageSurveyRecyclerAdapter
import com.maxi.dogapi.databinding.ActivityWelcomeCalaBinding
import com.maxi.dogapi.databinding.ActivityWelcomeCalaSurveyVillageBinding
import com.maxi.dogapi.utils.NetworkResult
import com.maxi.dogapi.viewmodel.WelcomeCalaVillageSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeCalaVillageSurveyActivity : AppCompatActivity() {

    private val welcomeCalaVillageSurveyViewModel by viewModels<WelcomeCalaVillageSurveyViewModel>()
    private lateinit var _binding: ActivityWelcomeCalaSurveyVillageBinding
    private lateinit var villageName: String
    private lateinit var villageId: String
    private lateinit var userId: String
    private lateinit var loginId: String
    private lateinit var orgOfficeId: String
    private lateinit var projectId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this, R.layout.activity_welcome_cala_survey_village);

//        _binding.tvUName.text="Hello,\n" + intent.getStringExtra("uName")+" !"

        villageName= intent.getStringExtra("village_name").toString()
        villageId= intent.getStringExtra("village_id").toString()
        loginId =intent.getStringExtra("login_id").toString()
        userId=intent.getStringExtra("user_id").toString()
        projectId=intent.getStringExtra("project_id").toString()
        orgOfficeId=intent.getStringExtra("org_office_id").toString()

        _binding.rvMain.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

/*
        Toast.makeText(
            this,
            villageId.toString()+ " "+loginId+userId,
            Toast.LENGTH_LONG
        ).show()
*/

        fetchData(villageId,loginId,projectId,userId,orgOfficeId)
//        fetchDataFromVillageClick()
//        welcomeCalaViewModel.clickLocationTrackingEvent.observe(this){event->
//            if (event){
//                val intent = Intent(this, CalaTrackingActivity::class.java)
//                intent.putExtra("survey_id", "")
//                startActivity(intent)
//            }
//
//        }
    }



    private fun fetchResponse(
        village_id: String,
        loginId: String,
        project_id: String,
        user_id: String,
        orgOfficeId: String
    ) {

        welcomeCalaVillageSurveyViewModel.fetchCalaDetailsResponse(village_id,loginId,project_id,user_id,orgOfficeId)
        _binding.pbDog.visibility = View.VISIBLE
    }

//    private fun fetchResponseFromVillageID(village_id: String, loginId: String, project_id:String, user_id: String, org_office_id: String) {
//
//        welcomeCalaViewModel.fetchCalaVillageIDDetailsResponse(village_id,loginId,project_id,user_id,org_office_id)
//        _binding.pbDog.visibility = View.VISIBLE
//    }


    private fun fetchData(
        villageId: String,
        loginId: String,
        project_id: String,
        user_id: String,
        orgOfficeId: String
    ) {
        fetchResponse(villageId, loginId,project_id,user_id,orgOfficeId)
        welcomeCalaVillageSurveyViewModel.response.observe(this) { response ->
            Log.e("RESPONSE","response"+response.toString())
/*
            Toast.makeText(
                this,
                response.toString(),
                Toast.LENGTH_SHORT
            ).show()
*/
            when (response) {
                is NetworkResult.Success -> {
/*
                    Toast.makeText(
                        this,
                        "response.toString()"+response.data?.calaVillageSurveyResponse?.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
*/

                    response.data?.calaVillageSurveyResponse?.let {
                        _binding.pbDog.visibility = View.GONE
/*
                        Toast.makeText(
                            this,
                            "response.toString()"+response.data?.calaVillageSurveyResponse?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
*/

//                        _binding.toolbarTitle.text= villageName.split("$").toString()

                        _binding.rvMain.adapter= response.data?.calaVillageSurveyResponse?.calaVill?.let { calaVill ->
                            CalaVillageSurveyRecyclerAdapter(welcomeCalaVillageSurveyViewModel,
                                calaVill, this)
                        }

                        Log.e("RESPONSE11","response12"+response.data?.calaVillageSurveyResponse?.toString())

//                        val intent = Intent(this, WelcomeActivity::class.java)
//                        intent.putExtra("uName", response.data.message[0].toString())
//                        startActivity(intent)

//                        _binding.imgDog.load(
//                            response.data.message
//                        ) {
////                            transformations(RoundedCornersTransformation(16f))
//                        }
                    }
//                    _binding.pbDog.visibility = View.GONE
                }

                is NetworkResult.Error -> {
//                    _binding.pbDog.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("RESPONSE111","response"+response.message?.toString())

                }

                is NetworkResult.Loading -> {
                    _binding.pbDog.visibility = View.VISIBLE
                }
            }
        }
    }

}
