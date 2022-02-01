package com.example.smartlocker.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Static(application: Application) : AndroidViewModel(application) {


    private val db by lazy {
        SmartLockerDatabase.getInstance(application)
    }
    private val sharedPref by lazy {
        application.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE)
    }

    val timeSetting = MutableLiveData<Int>(sharedPref.getInt("timeOption", 24))

    //비정상 사용자의 사용시간을 구하고 기준 이상 사용자를 필터링 후, 리스트에 담는 함수
    @SuppressLint("SimpleDateFormat")
    fun getAbnormalList(): MutableList<AbnormalData> {
        val currentTime = System.currentTimeMillis()
        val abnormalList = mutableListOf<AbnormalData>()

        CoroutineScope(Dispatchers.IO).launch {

            db?.getNodeDao()?.getAll()?.filter { it.enabled }?.forEach {
                val usingTimeLong = currentTime - it.getTime!!
                val usingTimeHour = ((usingTimeLong / 1000) / 60) / 60

                if (usingTimeHour >= timeSetting.value!!) {
                    val usingTimeMin = ((usingTimeLong / 1000) / 60) % 60
                    val timeString = "${usingTimeHour}시간 ${usingTimeMin}분 사용"
                    abnormalList.add(AbnormalData(it.id, timeString))
                }
            }
        }
        return abnormalList
    }

    //비정상 사용자 메뉴옵션 값을 설정 및 저장
    fun setSharedPrefTimeSetting(time: Int, activity: Activity) {
        val sharedPref =
            activity.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt("timeOption", time)
            commit()
        }
        timeSetting.value = sharedPref.getInt("timeOption", 48)
    }

    suspend fun setDataEntries(): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            db?.getStaticTimeDao()?.getAll()?.forEachIndexed { index, it ->
                entries.add(BarEntry(index.toFloat() + 1 , it.maxAmount.toFloat()))
            }
        }

        return entries
    }

}
