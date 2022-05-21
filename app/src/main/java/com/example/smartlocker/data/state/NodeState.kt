package com.example.smartlocker.data.state

import android.content.Context

//노드 상태에 따라 로직이 달라지기 때문에 state pattern 을 따른다.
interface NodeState {
    fun nodeClick(context: Context, id:Int)
}