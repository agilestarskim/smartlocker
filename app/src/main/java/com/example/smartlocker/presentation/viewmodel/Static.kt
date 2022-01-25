package com.example.smartlocker.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.SystemClock
import android.util.Log
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Static(application: Application) {


    private val db by lazy {
        SmartLockerDatabase.getInstance(application)
    }

    @SuppressLint("SimpleDateFormat")
    fun getAbnormalList(): MutableList<AbnormalData>{
        val currentTime = SystemClock.elapsedRealtime()
        val abnormalList = mutableListOf<AbnormalData>()

        CoroutineScope(Dispatchers.IO).launch {

            db?.getNodeDao()?.getAll()?.filter { it.enabled }?.forEach {
                val usingTimeMin = (currentTime - it.getTime!!)/1000/60
                val usingTimeHour = usingTimeMin/60
                val time : String = "${usingTimeHour}시간 ${usingTimeMin}분 사용"
                abnormalList.add(AbnormalData(it.id, time))
            }
        }
        return abnormalList
    }


}