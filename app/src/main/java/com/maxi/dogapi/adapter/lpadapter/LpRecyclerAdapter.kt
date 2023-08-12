package com.maxi.dogapi.adapter.lpadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.maxi.dogapi.R
import com.maxi.dogapi.model.lp.LpResponseArray
import com.maxi.dogapi.viewmodel.WelcomeLPViewModel
import kotlinx.android.synthetic.main.lp_item_view.view.*

class LpRecyclerAdapter(val viewModel: WelcomeLPViewModel, val arrayList: ArrayList<LpResponseArray>, val context: Context): RecyclerView.Adapter<LpRecyclerAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LpRecyclerAdapter.NotesViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.lp_item_view,parent,false)
        return NotesViewHolder(root)
    }

    override fun onBindViewHolder(holder: LpRecyclerAdapter.NotesViewHolder, position: Int) {
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
        fun bind(data: LpResponseArray){
            binding.tvPName.text = data.project_name
            binding.tvAddDetail.text = data.village_name+ ""+data.sub_district_name+""+data.district_name+""+data.state_name
            binding.tvCalaDetail.text = "Cala name: "+data.cala_name
            binding.tvSurveyDetail.text = "Survey info: "+data.survey_no+" "+data.survey_no_hin
            binding.tvAreaDetail.text = "Area/Total Area: "+data.area+"/"+data.total_area
            binding.tvANotificationDetail.text = "laNotification:"+data.laNotification+"\nthreeANotification:"+data.laNotification+"\nthreeDNotification:"+data.laNotification+"\ncompensationAmt:"+data.compensationAmt





                    binding.rootView.setOnClickListener {
                viewModel.startTrackingClass()
            }
        }

    }
}