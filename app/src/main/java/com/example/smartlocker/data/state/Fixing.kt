package com.example.smartlocker.data.state

import android.content.Context
import android.util.Log
import android.widget.Toast

//사용자 모드에서 선택한 사물함이 점검 중일 때
class Fixing: NodeState {
    //사물함을 클릭했을 때 컨텍스트와 사물함 번호를 전달받는다.
    override fun nodeClick(context: Context, id:Int) {
        Toast.makeText(context,"점검 중 입니다.", Toast.LENGTH_SHORT).show()
    }
}