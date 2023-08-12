package com.maxi.dogapi.adapter.villaggesurveyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.maxi.dogapi.R
import com.maxi.dogapi.model.cala.CalaSubDistrictResponseArray
import com.maxi.dogapi.model.cala.CalaVillageResponseArray
import com.maxi.dogapi.model.villagesurvey.CalaVillageSurveyResponseArray
import com.maxi.dogapi.viewmodel.WelcomeCalaViewModel
import com.maxi.dogapi.viewmodel.WelcomeCalaVillageSurveyViewModel
import kotlinx.android.synthetic.main.cala_sub_district_item_view.view.*
import kotlinx.android.synthetic.main.cala_village_survey_item_view.view.*
import kotlinx.android.synthetic.main.lp_item_view.view.*
import kotlinx.android.synthetic.main.lp_item_view.view.tvPName


class CalaVillageSurveyRecyclerAdapter(val viewModel: WelcomeCalaVillageSurveyViewModel, val arrayList: ArrayList<CalaVillageSurveyResponseArray>, val context: Context): RecyclerView.Adapter<CalaVillageSurveyRecyclerAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalaVillageSurveyRecyclerAdapter.NotesViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.cala_village_survey_item_view,parent,false)
        return NotesViewHolder(root)
    }

    override fun onBindViewHolder(holder: CalaVillageSurveyRecyclerAdapter.NotesViewHolder, position: Int) {
        holder.bind(arrayList.get(position))
    }

    override fun getItemCount(): Int {
        if(arrayList.size==0){
            Toast.makeText(context,"List is empty", Toast.LENGTH_LONG).show()
        }else{

        }
        return arrayList.size
    }


    inner  class NotesViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(data: CalaVillageSurveyResponseArray){
            binding.tvSurveyNumb.text = "Survey number: "+data.surveyName





//                    binding.rootView.setOnClickListener {
//                viewModel.clickHandleVillages(data.calaVill)
//            }
        }

    }

}