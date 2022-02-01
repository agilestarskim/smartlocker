package com.example.smartlocker.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.data.room.NodeModel
import com.example.smartlocker.data.room.StaticTimeModel
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.*

class LiveNode(application: Application) : AndroidViewModel(application) {

    //데이터베이스 생성
    private val db by lazy {
        SmartLockerDatabase.getInstance(application)
    }

    //라이브데이터
    companion object{
        private val _liveNodeList = MutableLiveData<List<NodeModel>>()
    }

    //외부에서 접근
    val liveNodeList: LiveData<List<NodeModel>>
        get() = _liveNodeList

    fun fetch() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = CoroutineScope(Dispatchers.IO).async {
                db?.getNodeDao()?.getAll()
            }
            _liveNodeList.value = result.await()

        }
    }

    suspend fun get(id: Int): NodeModel? {
        val result = CoroutineScope(Dispatchers.IO).async {
            db?.getNodeDao()?.get(id)
        }
        return result.await()
    }

    fun insert(node: NodeModel) {
        CoroutineScope(Dispatchers.IO).launch {
            db?.getNodeDao()?.insert(node)
            fetch()
            insertStaticTime()

        }
    }

    fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            launch { db?.getNodeDao()?.delete(id) }.join()
            launch { fetch() }

        }

    }

    fun deleteAll() {
        db?.getNodeDao()?.deleteAll()
        _liveNodeList.value = db?.getNodeDao()?.getAll()
    }

    private fun insertStaticTime(){
            val time: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            val currentAmount = db?.getNodeDao()?.getAll()?.filter { it.enabled }?.size?:0
            if(db?.getStaticTimeDao()?.get(time) == null){
                db?.getStaticTimeDao()?.insert(StaticTimeModel(time,currentAmount,1))
            }else{
                val maxAmount = db?.getStaticTimeDao()?.get(time)?.maxAmount
                if(currentAmount > maxAmount!!){
                    when((currentAmount.toFloat()/5.0*100).toInt()){
                        in 0..20 -> db?.getStaticTimeDao()?.insert(StaticTimeModel(time,currentAmount,1))
                        in 21..99 -> db?.getStaticTimeDao()?.insert(StaticTimeModel(time,currentAmount,2))
                        100 -> db?.getStaticTimeDao()?.insert(StaticTimeModel(time,currentAmount,3))
                    }
                }
            }
    }
}