package com.example.smarttag.test


import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.maxi.dogapi.R
import com.maxi.dogapi.SchoolSpinnerAdapter
import java.io.ByteArrayInputStream
import java.io.InputStream

class ContactsAdapter(private var activityList: ArrayList<Contact>?, private val listener:IContactsAdapter) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val observation = itemView.findViewById<EditText>(R.id.etObservation)
        val spinnerActivity = itemView.findViewById<Spinner>(R.id.spinner_activity)
        val imageView = itemView.findViewById<ImageView>(R.id.ivImage)
        val btCamera = itemView.findViewById<ImageButton>(R.id.btCamera)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView: View = inflater.inflate(R.layout.recycler_item, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact? = activityList?.get(position)

        holder.observation.setText(contact?.observation)
        if(!TextUtils.isEmpty(contact?.image)){
            val decodedString: ByteArray = Base64.decode(contact?.image, Base64.NO_WRAP)
            val inputStream: InputStream = ByteArrayInputStream(decodedString)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            holder.imageView.setImageBitmap(bitmap)
        }
        holder.btCamera.setOnClickListener { listener.addImage(position) }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return activityList?.size?:0
    }

    private fun initActivityData(holder: ViewHolder, levelId:String, tpqaId:String, stateId:String) {
//        viewModel.fetchSchoolResponse(userId, levelId, tpqaId,stateId)
//        viewModel.schoolList.observe(this) {
//            adapterSchool = SchoolSpinnerAdapter(this, it)
//            binding.spinnerSchool.adapter = adapterSchool
//            binding.spinnerSchool.setSelection(0)
//
//        }
//        holder.spinnerActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//                val clickedItem: String = parent.getItemAtPosition(position) as String
//                val name: String = clickedItem
//                Toast.makeText(this@SurveySubmitActivity, "$name selected", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
    }



    interface IContactsAdapter{
        fun addImage(position: Int)
        fun addObservation(position: Int)
        fun addActivity(position: Int)
    }
}