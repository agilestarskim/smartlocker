package com.example.smartlocker.presentation.view.activity.DashBoard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.smartlocker.R
import com.example.smartlocker.data.room.StaticDateModel

class StaticDateListViewAdapter (private val items: MutableList<StaticDateModel>): BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): StaticDateModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.static_date_listview_cell, parent, false)

        val stateTextView = view.findViewById<TextView>(R.id.stateTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val item: StaticDateModel = items[position]


        when(item.state){
            1 -> {
                stateTextView.setTextColor(Color.parseColor("#3BAFDA"))
                stateTextView.text = "여유"
            }
            2 -> {
                stateTextView.setTextColor(Color.parseColor("#8CC152"))
                stateTextView.text = "적절"
            }
            3 -> {
                stateTextView.setTextColor(Color.parseColor("#DA4453"))
                stateTextView.text = "부족"
            }
        }
        dateTextView.text = item.date

        return view
    }
}