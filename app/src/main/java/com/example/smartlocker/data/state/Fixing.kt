package com.example.smartlocker.data.state

import android.content.Context
import android.util.Log
import android.widget.Toast

class Fixing: NodeState {
    override fun nodeClick(context: Context, id:Int) {
        Toast.makeText(context,"점검 중 입니다.", Toast.LENGTH_SHORT).show()
    }
}