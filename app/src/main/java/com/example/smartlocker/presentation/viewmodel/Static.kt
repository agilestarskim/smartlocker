package com.example.smartlocker.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Static(application: Application) : AndroidViewModel(application) {


    private val db by lazy {
        SmartLockerDatabase.getInstance(application)
    }
    private val sharedPref by lazy{
        application.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE)
    }

    val timeSetting = MutableLiveData<Int>(sharedPref.getInt("timeOption", 24))

    @SuppressLint("SimpleDateFormat")
    fun getAbnormalList(): MutableList<AbnormalData>{
        val currentTime = System.currentTimeMillis()
        val abnormalList = mutableListOf<AbnormalData>()

        CoroutineScope(Dispatchers.IO).launch {

            db?.getNodeDao()?.getAll()?.filter { it.enabled }?.forEach {
                val usingTimeLong = currentTime - it.getTime!!
                val usingTimeHour = ((usingTimeLong/1000)/60)/60

                if(usingTimeHour >= timeSetting.value!!){
                    val usingTimeMin = ((usingTimeLong/1000)/60)%60
                    val timeString  = "${usingTimeHour}시간 ${usingTimeMin}분 사용"
                    abnormalList.add(AbnormalData(it.id, timeString))
                }
            }
        }
        return abnormalList
    }

    fun setSharedPrefTimeSetting(time: Int, activity: Activity){
        val sharedPref = activity.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE) ?:return
        with(sharedPref.edit()){
            putInt("timeOption", time)
            commit()
        }
        timeSetting.value = sharedPref.getInt("timeOption", 48)
    }
}