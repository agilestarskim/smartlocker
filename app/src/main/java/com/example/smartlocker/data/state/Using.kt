package com.example.smartlocker.data.state

import android.content.Context
import android.util.Log
import com.example.smartlocker.data.state.NodeState
import com.example.smartlocker.presentation.view.dialog.AssignDialog
import com.example.smartlocker.presentation.view.dialog.CheckDialog

//사용자 모드에서 선택한 사물함이 이미 사용중일 때
class Using: NodeState {
    //사물함을 클릭했을 때 컨텍스트와 사물함 번호를 전달받는다.
    override fun nodeClick(context: Context, id:Int) {
        //해당사물함 사용자가 맞는지 비밀번호 입력창이 보여진다.
        val dialog = CheckDialog(context, id)
        dialog.show()
    }
}