package com.example.smartlocker.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartlocker.databinding.ActivityDashboardBinding
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalListViewAdapter
import com.example.smartlocker.presentation.viewmodel.Static

class DashBoardActivity: AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val static by lazy{
        Static(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClickListener()
        initAbnormalListView()
    }

    private fun initClickListener(){
        binding.exitButton.setOnClickListener(this)
    }

    private fun initAbnormalListView(){
        val abnormalList = static.getAbnormalList()
        val adapter = AbnormalListViewAdapter(abnormalList)
        binding.abnormalListView.adapter = adapter
    }


    override fun onClick(v: View?) {
        when(v){
            binding.exitButton -> {
                val intent = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, intent, null)
            }
        }

    }
}