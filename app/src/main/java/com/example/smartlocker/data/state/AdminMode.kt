package com.example.smartlocker.data.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//관리자 모드
object AdminMode {
    //관리자 모드가 켜지면 true 사용자모드면 false
    val liveState = MutableLiveData<Boolean>(false)
    //1. 열기 2. 점검 중
    var mode:Int = 0
}