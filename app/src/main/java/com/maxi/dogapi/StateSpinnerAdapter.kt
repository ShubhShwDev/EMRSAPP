package com.maxi.dogapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.maxi.dogapi.model.state.StateDetail

class StateSpinnerAdapter(val context: Context, var dataSource: List<StateDetail>) : BaseAdapter()
 {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getItem(position: Int): Any? {
        return dataSource[position].name;
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.text_view) as TextView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        val currentItem = getItem(position)
        val currentItem1 = currentItem
        vh.label.text = currentItem1.toString()

        return view
    }

//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return initView(position, convertView!!, parent)
//    }

//    private fun initView(
//        position: Int, convertView: View,
//        parent: ViewGroup
//    ): View {
//        // It is used to set our custom view.
//        var convertVieew = convertView
////        if (convertView == null) {
//        convertVieew =
//                LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
////        }
//        val textViewName = convertView.findViewById<TextView>(R.id.text_view)
//        val currentItem = getItem(position)
//
//        // It is used the name to the TextView when the
//        // current item is not null.
//        if (currentItem != null) {
//            textViewName.text = currentItem.name
//        }
//        return convertView
//    }
}