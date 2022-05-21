package com.example.smartlocker.data.state
import android.content.Context

import com.example.smartlocker.presentation.view.dialog.AssignDialog

//사용자모드에서 선택한 사물함이 사용가능할 때
class Available: NodeState {
    //사물함을 클릭했을 때 컨텍스트와 사물함 번호를 전달받는다.
    override fun nodeClick(context: Context, id:Int){
        //"사물함을 배정받으시겠습니까? 예:아니오" 다이얼로그가 보여진다.
        val dialog = AssignDialog(context, id)
        dialog.show()
    }
}