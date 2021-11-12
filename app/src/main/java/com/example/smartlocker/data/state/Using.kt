package com.example.smartlocker.data.state

import android.content.Context
import android.util.Log
import com.example.smartlocker.data.state.NodeState
import com.example.smartlocker.presentation.view.dialog.AssignDialog
import com.example.smartlocker.presentation.view.dialog.CheckDialog

class Using: NodeState {
    override fun nodeClick(context: Context, id:Int) {
        val dialog = CheckDialog(context, id)
        dialog.show()
    }
}