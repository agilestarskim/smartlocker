package com.example.smartlocker.data.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object AdminMode {
    val liveState = MutableLiveData<Boolean>(false)
    //1. 열기 2. 점검 중
    var mode:Int = 0
}