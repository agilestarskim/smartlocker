package com.example.smartlocker.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.smartlocker.R
import com.example.smartlocker.databinding.ActivityDashboardBinding
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalListViewAdapter
import com.example.smartlocker.presentation.viewmodel.Static

class DashBoardActivity: AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val static by lazy {
        ViewModelProvider(this)[Static::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
        observeTimeSetting()
    }

    private fun initClickListener(){
        binding.exitButton.setOnClickListener(this)
        binding.timeSettingToggleButton.setOnClickListener(this)
    }

    private fun observeTimeSetting(){
        static.timeSetting.observe(this,{
            initAbnormalListView()
        })
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

            binding.timeSettingToggleButton -> {
                showPopup(v)
            }
        }
    }

    private fun showPopup(v: View){
        val popup = PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@DashBoardActivity)
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.popup, popup.menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.timeSetting1 -> {
                static.setSharedPrefTimeSetting(24, this)
                true
            }
            R.id.timeSetting2 -> {
                static.setSharedPrefTimeSetting(48, this)
                true
            }
            R.id.timeSetting3 -> {
                static.setSharedPrefTimeSetting(72, this)
                true
            }

            else -> {false}
        }
    }


}