package com.example.smartlocker.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.data.room.StaticDateModel
import com.example.smartlocker.data.room.StaticTimeModel
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

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
    suspend fun getAbnormalList(): MutableList<AbnormalData> {
        val currentTime = System.currentTimeMillis()
        val abnormalList = mutableListOf<AbnormalData>()

         val result= CoroutineScope(Dispatchers.IO).async {
            db?.getNodeDao()?.getAll()?.filter { it.enabled }?.forEach {
                val usingTimeLong = currentTime - it.getTime!!
                val usingTimeHour = ((usingTimeLong / 1000) / 60) / 60

                if (usingTimeHour >= timeSetting.value!!) {
                    val usingTimeMin = ((usingTimeLong / 1000) / 60) % 60
                    val timeString = "${usingTimeHour}시간 ${usingTimeMin}분 사용"
                    abnormalList.add(AbnormalData(it.id, timeString))
                }
            }
            return@async abnormalList
        }
        return result.await()
    }

    suspend fun getDateList(): MutableList<StaticDateModel> {
        val result = CoroutineScope(Dispatchers.IO).async {
            val staticDateList = mutableListOf<StaticDateModel>()
            db?.getStaticDayDao()?.getAll()?.filterNot { it.state == 0 }?.forEach {
                staticDateList.add(it)
            }
            return@async staticDateList
        }
        return result.await()
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

        val result = CoroutineScope(Dispatchers.IO).async{
            val entries = ArrayList<BarEntry>()
            db?.getStaticTimeDao()?.getAll()?.forEachIndexed { index, it ->
                entries.add(BarEntry(index.toFloat() + 1 , it.maxAmount.toFloat()))
            }
            return@async entries
        }

        return result.await()
    }

/**
 * timeData : 해당 시간에 데이터가 null 이여서 차트가 깨지는 것을 방지하기 위한 DB 초기화
 */
    fun initDataEntries(){
        CoroutineScope(Dispatchers.IO).launch{
            for (time in 8..21){
                if (db?.getStaticTimeDao()?.get(time) == null){
                    db?.getStaticTimeDao()?.insert(StaticTimeModel(time,0,1))
                }else{
                    break
                }
            }
        }
    }

    suspend fun setResult():Int{
        val list:MutableList<StaticDateModel> = getDateList()
        var totalValue : Int = 0
        for (index in 0..9){
            try {
                totalValue += list[index].state
            }catch (e : IndexOutOfBoundsException){
                break
            }
        }
        val averageValue = round(totalValue/(list.size.toFloat()))
        return averageValue.toInt()
    }



    fun setStatic(action: Int){
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
        val dateString = formatter.format(System.currentTimeMillis())
        if(currentTime in 8..21){
            setStaticDay(dateString)
            setStaticTime(currentTime, action)
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

    private fun setStaticTime(time:Int, action:Int){
        val currentAmount :Int = db?.getNodeDao()?.getAll()?.filter { it.enabled }?.size?: 0
        var maxAmount :Int = db?.getStaticTimeDao()?.get(time)?.maxAmount?:currentAmount
        if(currentAmount >= maxAmount){
            maxAmount = currentAmount
            val state = getTimeState(maxAmount)
            db?.getStaticTimeDao()?.insert(StaticTimeModel(time,maxAmount,state))
        }
        setStaticTimeBefore(currentAmount, time, action)

    }

    private fun setStaticTimeBefore(currentAmount: Int, time:Int, action: Int){
        var start = time - 1
        if(action == 1){
            val maxAmount = currentAmount - 1
            val state = getTimeState(maxAmount)
            while (db?.getStaticTimeDao()?.get(start)?.maxAmount == 0){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(start, maxAmount, state))
                start --
            }
        }else if(action == 2){
            val maxAmount = currentAmount + 1
            val state = getTimeState(maxAmount)
            while (db?.getStaticTimeDao()?.get(start)?.maxAmount == 0){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(start, maxAmount, state))
                start --
            }
        }else if(action == 3){
            if(currentAmount == 1) return
            val state = getTimeState(currentAmount)
            while (db?.getStaticTimeDao()?.get(start)?.maxAmount == 0){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(start, currentAmount, state))
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
