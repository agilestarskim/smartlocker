package com.example.smartlocker.data.state

import android.content.Context

interface NodeState {
    fun nodeClick(context: Context, id:Int)
}