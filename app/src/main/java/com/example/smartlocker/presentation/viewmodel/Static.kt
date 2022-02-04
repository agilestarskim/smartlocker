package com.example.smartlocker.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.data.room.StaticDateModel
import com.example.smartlocker.data.room.StaticTimeModel
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Static(application: Application) : AndroidViewModel(application) {


    private val db by lazy {
        SmartLockerDatabase.getInstance(application)
    }
    private val sharedPref by lazy {
        application.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE)
    }

    val timeSetting = MutableLiveData<Int>(sharedPref.getInt("timeOption", 24))

    /**abnormal : 비정상 사용자의 사용시간을 구하고 기준 이상 사용자를 필터링 후, 리스트에 담는다.
     *
     */
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

    /**abnormal : 비정상 사용자 메뉴옵션 값을 설정 및 저장
     *
     */
    fun setSharedPrefTimeSetting(time: Int, activity: Activity) {
        val sharedPref =
            activity.getSharedPreferences("staticSettingOption", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt("timeOption", time)
            commit()
        }
        timeSetting.value = sharedPref.getInt("timeOption", 48)
    }

    /**
     * timeData : 타임데이터를 모두가져와 Bar Entry 에 담는다.
     */
    suspend fun setDataEntries(): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            db?.getStaticTimeDao()?.getAll()?.forEachIndexed { index, it ->
                entries.add(BarEntry(index.toFloat() + 1 , it.maxAmount.toFloat()))
            }
        }

        return entries
    }

/**
 * timeData : 해당 시간에 데이터가 null 이여서 차트가 깨지는 것을 방지하기 위한 DB 초기화
 */
    suspend fun initDataEntries(){
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
            for (time in 8..21){
                if (db?.getStaticTimeDao()?.get(time) == null){
                    db?.getStaticTimeDao()?.insert(StaticTimeModel(time,0,1))
                }else{
                    break
                }
            }
        }
    }

    fun setStatic(isInsert: Boolean){
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
        val dateString = formatter.format(System.currentTimeMillis())
        if(currentTime in 8..21){
            setStaticDay(dateString)
            setStaticTime(currentTime, isInsert)
        }
    }

    private fun setStaticDay(inputDate: String){
        val date : String? = db?.getStaticDayDao()?.get(inputDate)?.date
        if(date == null){
            db?.getStaticDayDao()?.update(getDayState())
            db?.getStaticDayDao()?.insert(StaticDateModel(inputDate, 0))
            initStaticTime()
        }
    }

    private fun getDayState() : Int{
        var state1 = 0
        var state2 = 0
        var state3 = 0
        db?.getStaticTimeDao()?.getAll()?.forEach {
            when(it.state){
                1 -> state1 ++
                2 -> state2 ++
                3 -> state3 ++
            }
        }
        return when {
            (state3 > 0)-> 3
            (state1 > 6 && state3 == 0) -> 1
            else -> 2
        }
    }

    private fun setStaticTime(time:Int, isInsert:Boolean){
        val currentAmount :Int = db?.getNodeDao()?.getAll()?.filter { it.enabled }?.size?: 0
        var maxAmount :Int = db?.getStaticTimeDao()?.get(time)?.maxAmount?:currentAmount
        if(currentAmount >= maxAmount){
            maxAmount = currentAmount
            val state = getTimeState(maxAmount)
            db?.getStaticTimeDao()?.insert(StaticTimeModel(time,maxAmount,state))
        }
        setStaticTimeBefore(currentAmount, time, isInsert)

    }

    private fun setStaticTimeBefore(currentAmount: Int, time:Int, isInsert: Boolean){
        var start = time - 1
        if(isInsert){
            val maxAmount = currentAmount - 1
            val state = getTimeState(maxAmount)
            while (db?.getStaticTimeDao()?.get(start)?.maxAmount == 0){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(start, maxAmount, state))
                start --
            }
        }else{
            val maxAmount = currentAmount + 1
            val state = getTimeState(maxAmount)
            while (db?.getStaticTimeDao()?.get(start)?.maxAmount == 0){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(start, maxAmount, state))
                start --
            }
        }


    }

    private fun getTimeState(amount : Int):Int{
        val totalSize = 5.0
        var state = 0
        when(((amount.toFloat()/totalSize)*100).toInt()){
            in 0..20 -> state = 1
            in 21..99 -> state = 2
            100 -> state = 3
        }
        return state
    }

    private fun initStaticTime(){
        for (time in 8..21){
            db?.getStaticTimeDao()?.insert(StaticTimeModel(time,0,1))
        }
    }


}
