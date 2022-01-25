package com.example.smartlocker.presentation.view.activity.DashBoard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.smartlocker.R

class AbnormalListViewAdapter(private val items: MutableList<AbnormalData>):BaseAdapter() {


    override fun getCount(): Int = items.size

    override fun getItem(position: Int): AbnormalData = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view:View = LayoutInflater.from(parent?.context).inflate(R.layout.abnormal_listview_cell, parent, false)

        val nodeNumberTextView = view.findViewById<TextView>(R.id.nodeNumberTextView)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)
        val item: AbnormalData = items[position]

        nodeNumberTextView.text = item.nodeNumber.toString()
        timeTextView.text = item.time

        return view
    }
}