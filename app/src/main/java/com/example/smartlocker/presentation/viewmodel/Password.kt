package com.example.smartlocker.presentation.viewmodel

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.example.smartlocker.bluetooth.MyBluetoothService
import com.example.smartlocker.data.room.NodeModel
import com.example.smartlocker.presentation.logic.Logic
import com.example.smartlocker.presentation.view.activity.MainActivity
import java.util.NoSuchElementException

class Password(application: Application): AndroidViewModel(application) {


    private val liveNode by lazy { LiveNode(application) }

    //비밀번호 확인 몇 번 틀렸는지 카운트
    private var count = 0

    //비밀번호 설정인지 확인인지 판별하는 토글
    private var _state = false
    private val _liveState = MutableLiveData<Boolean>()
    //첫번째 비밀번호 배열
    private val _firstPassword = mutableListOf<Char>()
    private val _liveFirstPassword = MutableLiveData<List<Char>>()
    //두번째 비밀번호 배열
    private val _secondPassword = mutableListOf<Char>()
    private val _liveSecondPassword = MutableLiveData<List<Char>>()
    //액티비티 뷰를 위한 토글
    //1:성공 2: 실패 3: 3회이상 초기화
    val nextView = MutableLiveData<Int>()

    //외부에서 접근할 때
    val firstPassword:LiveData<List<Char>>
        get() = _liveFirstPassword

    val secondPassword:LiveData<List<Char>>
        get() = _liveSecondPassword

    val state:LiveData<Boolean>
        get() = _liveState

    fun add(num:Char){
        if(_state.not()){
            _firstPassword.add(num)
            _liveFirstPassword.value = _firstPassword
        }else{
            _secondPassword.add(num)
            _liveSecondPassword.value = _secondPassword
        }
        //첫번째 배열이 다 차면
        if(_firstPassword.size == 6){
            _state = true
            _liveState.value = true
        }
        //두번째 배열이 다 차면
        if(_secondPassword.size == 6){
           checkPassword()
        }

    }

    fun delete(){
        if(_state.not()){
            try {
                _firstPassword.removeLast()
            }catch (e: NoSuchElementException){
                return
            }
            _liveFirstPassword.value = _firstPassword
        }
        else{
            try {
                _secondPassword.removeLast()
            }catch (e: NoSuchElementException ){
                return
            }
            _liveSecondPassword.value = _secondPassword
        }
    }

    fun deleteAll(){
        _state = false
        _liveState.value = false
        _firstPassword.clear()
        _liveFirstPassword.value = _firstPassword
        _secondPassword.clear()
        _liveSecondPassword.value = _secondPassword
    }

    private fun checkPassword(){
        //비밀번호가 일치 성공
        if(_firstPassword == _secondPassword){
            complete()
            nextView.value = 1
        }
        //비밀번호 불일치
        else{
            count++
            //3회 전까지
            if(count<3){
                _secondPassword.clear()
                _liveSecondPassword.value = _secondPassword
                nextView.value = 2
            }
            //불일치 3회시 초기화
            else{
                deleteAll()
                count = 0
                nextView.value = 3
            }
        }
    }

    private fun complete(){
        //nodeDB 저장
        val node = NodeModel(
            id = Logic.getSelectedId(),
            password = _secondPassword.joinToString(""),
            enabled = true,
            getTime = System.currentTimeMillis()
        )
        liveNode.insert(node)

        //TODO OPEN
        MyBluetoothService.g_socket?.let {
            MyBluetoothService().ConnectedThread(it,Logic.getSelectedId()).run()
        }
    }

}