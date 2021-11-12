package com.example.smartlocker.presentation.logic

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smartlocker.data.state.AdminMode
import com.example.smartlocker.data.state.Available
import com.example.smartlocker.data.state.Using
import com.example.smartlocker.presentation.viewmodel.LiveNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Logic(application: Application) {
    private val liveNode by lazy { LiveNode(application) }

    companion object{
        private var selectedID: Int = 0
        fun setSelectedId(id: Int) {
            selectedID = id
        }

        fun getSelectedId(): Int {
            return selectedID
        }
    }


    //메인에서 노드를 클릭했을 때
    fun onClick(context: Context, id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val node = liveNode.get(id)
            setSelectedId(id)
            //관리자 모드
            if (AdminMode.state) {
                Log.d("myTag", "관리자 모드입니다. ")
                when (AdminMode.mode) {
                    //열기 모드
                    (1) -> {
                        //사용가능
                        if(node == null){
                            Toast.makeText(context,"사용 중인 사물함을 선택해 주세요",Toast.LENGTH_SHORT).show()
                            AdminMode.state = false
                        }
                        //사용 중
                        else{
                            Log.d("myTag", "사물함 강제로 열기 ")
                            AdminMode.state = false
                        }
                    }
                    //점검 중 모
                    (2) -> {
                        if(node == null){
                            Log.d("myTag", "사물함 점검 중으로 만들기 ")
                            AdminMode.state = false
                        }
                        else{
                            Toast.makeText(context,"사용중인 사물함 입니다. 반납후 설정해 주세요",Toast.LENGTH_SHORT).show()
                            AdminMode.state = false
                        }
                    }
                }
            }

            //사용자 모드
            else {
                Log.d("myTag", "사용자 모드입니다. ")
                if (node == null) {
                    Available().nodeClick(context, getSelectedId())
                }
                //사용중이면
                else {
                    Using().nodeClick(context, getSelectedId())
                }
            }
        }
    }
}