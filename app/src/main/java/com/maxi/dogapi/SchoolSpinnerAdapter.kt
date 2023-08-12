package com.maxi.dogapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.maxi.dogapi.model.school.SchoolDetails

class SchoolSpinnerAdapter(
    context: Context?,
    schoolList: List<SchoolDetails>
) : ArrayAdapter<SchoolDetails?>(
    context!!, 0, schoolList!!
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView!!, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView!!, parent)
    }

    private fun initView(
        position: Int, convertView: View,
        parent: ViewGroup
    ): View {
        // It is used to set our custom view.
        var convertView = convertView
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
        }
        val textViewName = convertView.findViewById<TextView>(R.id.text_view)
        val currentItem = getItem(position)

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.text = currentItem.name
        }
        return convertView
    }
}