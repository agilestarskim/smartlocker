package com.example.smartlocker.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.smartlocker.data.room.SmartLockerDatabase
import com.example.smartlocker.data.room.NodeModel
import kotlinx.coroutines.*

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



}