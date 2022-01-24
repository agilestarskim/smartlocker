package com.example.smartlocker.data.state
import android.content.Context

import com.example.smartlocker.presentation.view.dialog.AssignDialog


class Available: NodeState {
    override fun nodeClick(context: Context, id:Int){
        val dialog = AssignDialog(context, id)
        dialog.show()
    }
}