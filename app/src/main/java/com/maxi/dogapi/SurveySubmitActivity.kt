package com.maxi.dogapi

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.maxi.dogapi.databinding.ActivitySubmitSurveyBinding
import com.maxi.dogapi.model.school.SchoolDetails
import com.maxi.dogapi.model.state.StateDetail
import com.maxi.dogapi.viewmodel.SubmitSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SurveySubmitActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySubmitSurveyBinding
    private lateinit var adapterState:StateSpinnerAdapter
    private lateinit var adapterSchool:SchoolSpinnerAdapter
    private val viewModel by viewModels<SubmitSurveyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_submit_survey)
        initStateData()
        initSchoolData()
    }

    private fun initSchoolData() {
        viewModel.schoolList.observe(this){
            adapterSchool = SchoolSpinnerAdapter(this, it)
            binding.spinnerSchool.adapter = adapterSchool
        }
        binding.spinnerSchool.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val clickedItem: SchoolDetails = parent.getItemAtPosition(position) as SchoolDetails
                val name: String = clickedItem.name
                Toast.makeText(this@SurveySubmitActivity, "$name selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initStateData() {
        val userId= intent.getStringExtra("userId").toString()
        val levelId =intent.getStringExtra("levelId").toString()
        val tpqaId=intent.getStringExtra("tpqaId").toString()
        viewModel.fetchStateResponse(userId,levelId,tpqaId )

       viewModel.stateList.observe(this){
           adapterState = StateSpinnerAdapter(this, it)
           binding.spinnerState.adapter = adapterState
       }
        binding.spinnerState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val clickedItem: StateDetail = parent.getItemAtPosition(position) as StateDetail
                val name: String = clickedItem.name
                Toast.makeText(this@SurveySubmitActivity, "$name selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}