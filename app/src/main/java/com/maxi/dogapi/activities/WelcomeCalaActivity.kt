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
import com.maxi.dogapi.adapter.calaadapter.CalaRecyclerAdapter
import com.maxi.dogapi.adapter.lpadapter.LpRecyclerAdapter
import com.maxi.dogapi.databinding.ActivityWelcomeBinding
import com.maxi.dogapi.databinding.ActivityWelcomeCalaBinding
import com.maxi.dogapi.utils.NetworkResult
import com.maxi.dogapi.viewmodel.WelcomeCalaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeCalaActivity : AppCompatActivity() {

    private val welcomeCalaViewModel by viewModels<WelcomeCalaViewModel>()
    private lateinit var _binding: ActivityWelcomeCalaBinding
    private lateinit var utId: String
    private lateinit var userId: String
    private lateinit var loginId: String
    private lateinit var orgOfficeId: String
    private lateinit var projectId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this, R.layout.activity_welcome_cala);


//        _binding.tvUName.text="Hello,\n" + intent.getStringExtra("uName")+" !"

        utId= intent.getStringExtra("utypeId").toString()
        loginId =intent.getStringExtra("loginId").toString()
        userId=intent.getStringExtra("user_id").toString()
        orgOfficeId=intent.getStringExtra("org_office_id").toString()

        _binding.rvMain.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

/*
        Toast.makeText(
            this,
            utId.toString()+ " "+loginId+userId,
            Toast.LENGTH_LONG
        ).show()
*/

        fetchData(utId,loginId,userId,orgOfficeId)
        fetchDataFromVillageClick()
//        welcomeCalaViewModel.clickLocationTrackingEvent.observe(this){event->
//            if (event){
//                val intent = Intent(this, CalaTrackingActivity::class.java)
//                intent.putExtra("survey_id", "")
//                startActivity(intent)
//            }
//
//        }
    }

    private fun fetchDataFromVillageClick() {
        welcomeCalaViewModel.villageLiveData.observe(this) { villageID ->
            Log.e("RESPONSE","response"+villageID.toString())
/*
            Toast.makeText(
                this,
                villageID.toString(),
                Toast.LENGTH_SHORT
            ).show()
*/

            val intent = Intent(this, WelcomeCalaVillageSurveyActivity::class.java)
                        intent.putExtra("village_id", villageID.split("$")[0].toString())
                        intent.putExtra("village_name", villageID.split("$")[1].toString())
                        intent.putExtra("login_id", loginId.toString())
                        intent.putExtra("project_id", projectId.toString())
                        intent.putExtra("user_id", userId.toString())
                        intent.putExtra("org_office_id", orgOfficeId.toString())
                        startActivity(intent)
        }
    }


    private fun fetchResponse(utId: String, loginId: String, user_id: String, org_office_id: String) {

        welcomeCalaViewModel.fetchCalaDetailsResponse(utId,loginId,user_id,org_office_id)
        _binding.pbDog.visibility = View.VISIBLE
    }

//    private fun fetchResponseFromVillageID(village_id: String, loginId: String, project_id:String, user_id: String, org_office_id: String) {
//
//        welcomeCalaViewModel.fetchCalaVillageIDDetailsResponse(village_id,loginId,project_id,user_id,org_office_id)
//        _binding.pbDog.visibility = View.VISIBLE
//    }


    private fun fetchData(utypeId: String, loginId: String, user_id: String, org_office_id: String) {
        fetchResponse(utypeId, loginId,user_id,org_office_id)
        welcomeCalaViewModel.response.observe(this) { response ->
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

                    response.data?.respmsg?.let {
                        _binding.pbDog.visibility = View.GONE
                        _binding.tvAgencyName.text="Agency:- "+response.data.respmsg.agency_name
                        _binding.toolbarTitle.text="Cala ("+response.data.respmsg.cala_name+")"
                        _binding.tvProjectName.text="Project:- "+response.data.respmsg.project_name
                        _binding.tvDistrictName.text="District:- "+response.data.respmsg.district_name
                        _binding.tvStateName.text="State:- " +response.data.respmsg.state_name
                        _binding.tvCalaName.text="Cala name:- "+response.data.respmsg.cala_name
                        projectId=response.data.respmsg.project_id

                        _binding.rvMain.adapter= response.data.respmsg?.calaSubDistrict?.let { it1 ->
                            CalaRecyclerAdapter(welcomeCalaViewModel,
                                it1, this)
                        }

                        Log.e("RESPONSE11","response"+response.data.respmsg?.calaSubDistrict?.toString())

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
                    Log.e("RESPONSE11","response"+response.message?.toString())

                }

                is NetworkResult.Loading -> {
                    _binding.pbDog.visibility = View.VISIBLE
                }
            }
        }
    }

}
