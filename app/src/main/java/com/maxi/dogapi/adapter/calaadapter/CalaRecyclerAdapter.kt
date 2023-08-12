package com.maxi.dogapi.adapter.calaadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.maxi.dogapi.R
import com.maxi.dogapi.model.cala.CalaSubDistrictResponseArray
import com.maxi.dogapi.model.cala.CalaVillageResponseArray
import com.maxi.dogapi.viewmodel.WelcomeCalaViewModel
import kotlinx.android.synthetic.main.cala_sub_district_item_view.view.*
import kotlinx.android.synthetic.main.layout2.view.*
import kotlinx.android.synthetic.main.lp_item_view.view.tvPName


class CalaRecyclerAdapter(val viewModel: WelcomeCalaViewModel, val arrayList: ArrayList<CalaSubDistrictResponseArray>, val context: Context): RecyclerView.Adapter<CalaRecyclerAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalaRecyclerAdapter.NotesViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.cala_sub_district_item_view,parent,false)
        return NotesViewHolder(root)
    }

    override fun onBindViewHolder(holder: CalaRecyclerAdapter.NotesViewHolder, position: Int) {
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
        fun bind(data: CalaSubDistrictResponseArray){
            binding.tvPName.text = "Sub District name: "+data.sub_Disttrict_Name

            data.calaVill?.let {
                showVillageList(data.calaVill, binding, binding.context)
            }



//            var index:Int= data.calaVill.find { it.village_name=="hello" }
//            binding.nsScrollll.setOnClickListener {
//                var position:Int = 0
//                if (it.tv.getTag() is Int) {
//                    position = it.tv.getTag() as Int
//                }
//                val index = binding.nsScrollll.indexOfChild(it.tv)
//                Toast.makeText(context,"List is empty"+index+ " "+position.toString(), Toast.LENGTH_LONG).show()
////            viewModel.clickHandleVillages(villageList[index-1].village_id,villageList[index-1].village_name)
//            }



//                    binding.rootView.setOnClickListener {
//                viewModel.clickHandleVillages(data.calaVill)
//            }
        }

    }

    fun showVillageList(
        villageList: ArrayList<CalaVillageResponseArray>,
        _binding: View,
        context: Context
    ){

        val N:Int = villageList.size; // total number of textviews to add
        val myTextViews = arrayOfNulls<TextView>(N) // create an empty array;


        val dim = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
//        val view: View = LayoutInflater.from(context).inflate(R.layout.layout2, null)

//        var tv1=TextView(context)
//        tv1.layoutParams = dim
//        view.tv.text = "Villages list - "
//        _binding.nsScrollll.addView(view)
        for (i in 0 until villageList.size) {

            val LnrChildLayout = LayoutInflater.from(context)
                .inflate(com.maxi.dogapi.R.layout.layout2, null)
//            var tv=TextView(view)
//            tv.layoutParams = dim
            LnrChildLayout.tv.text = " "+(i.plus(1)).toString()+". "+villageList[i].village_name
//            LnrChildLayout.tv.setTag(i);
            _binding.nsScrollll.addView(LnrChildLayout)
            val indexValue: Int =  _binding.nsScrollll.indexOfChild(LnrChildLayout)
            LnrChildLayout.tag = Integer.toString(indexValue)

            LnrChildLayout.setOnClickListener {
                try {
                    var index:String= it.tag as String
                    val finalIndex = index.toInt()
                    var village_id= villageList[finalIndex].village_id
                    var village_name=villageList[finalIndex].village_name

                    Toast.makeText(
                        context,
                        "List is empty" + index,
                        Toast.LENGTH_LONG
                    ).show()

                    viewModel.clickHandleVillages(
                        village_id,
                        village_name
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "List is empty" + e.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }



            }

        }





//        _binding.nsScrollll.addView( _binding.nsScrollllchild)
    }
}