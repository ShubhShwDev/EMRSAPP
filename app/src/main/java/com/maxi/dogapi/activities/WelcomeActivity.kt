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
import com.maxi.dogapi.adapter.lpadapter.LpRecyclerAdapter
import com.maxi.dogapi.databinding.ActivityWelcomeBinding
import com.maxi.dogapi.utils.NetworkResult
import com.maxi.dogapi.viewmodel.WelcomeLPViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private val welcomeLPViewModel by viewModels<WelcomeLPViewModel>()
    private lateinit var _binding: ActivityWelcomeBinding
    private lateinit var utId: String
    private lateinit var userId: String
    private lateinit var loginId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this, R.layout.activity_welcome);


        _binding.toolbarTitle.text="Land Party"
        utId= intent.getStringExtra("utypeId").toString()
        loginId =intent.getStringExtra("loginId").toString()
        userId=intent.getStringExtra("user_id").toString()

        _binding.rvMain.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//        Toast.makeText(
//            this,
//            utId.toString()+ " "+loginId+userId,
//            Toast.LENGTH_SHORT
//        ).show()

        fetchData(utId,loginId,userId)
        Log.e("FETCH_DATA","login"+loginId+" "+userId+" "+utId)
        welcomeLPViewModel.clickLocationTrackingEvent.observe(this){event->
            if (event){
                val intent = Intent(this, CalaTrackingActivity::class.java)
                intent.putExtra("survey_id", "")
                startActivity(intent)
            }

        }

        Log.e("FETCH_DATA","login"+loginId+" "+userId+" "+utId)

        welcomeLPViewModel.clickLocationTrackingEvent.observe(this){event->
            if (event){
                val intent = Intent(this, CalaTrackingActivity::class.java)
                intent.putExtra("survey_id", "")
                startActivity(intent)
            }

        }
    }

    private fun fetchData(utypeId: String, loginId: String, user_id: String) {
//        fetchResponse("5", "95524.73741","583005")
        fetchResponse(utypeId, loginId,user_id)
        welcomeLPViewModel.response.observe(this) { response ->
            Log.e("RESPONSE","response"+response.toString())

            when (response) {
                is NetworkResult.Success -> {

                    response.data?.let {
                        _binding.pbDog.visibility = View.GONE
                        _binding.rvMain.adapter= LpRecyclerAdapter(welcomeLPViewModel, response.data.message, this)

                        Log.e("RESPONSE1","response"+response.data.message.toString())

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
                }

                is NetworkResult.Loading -> {
                    _binding.pbDog.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun fetchResponse(utId: String, loginId: String, user_id: String) {

        welcomeLPViewModel.fetchLpDetailsResponse(utId,loginId,user_id)
        _binding.pbDog.visibility = View.VISIBLE
    }
}
