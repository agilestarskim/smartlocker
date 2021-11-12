package com.example.smartlocker.data.state

import android.content.Context
import android.util.Log

class Fixing: NodeState {
    override fun nodeClick(context: Context, id:Int) {
        Log.d("myTag", "점검 중")
    }
}