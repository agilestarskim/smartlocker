package com.example.smartlocker.presentation.logic

import android.app.Application
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smartlocker.bluetooth.MyBluetoothService
import com.example.smartlocker.data.room.NodeModel
import com.example.smartlocker.data.state.AdminMode
import com.example.smartlocker.data.state.Available
import com.example.smartlocker.data.state.Fixing
import com.example.smartlocker.data.state.Using
import com.example.smartlocker.presentation.view.activity.MainActivity
import com.example.smartlocker.presentation.view.dialog.OpenDialog
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
            if (AdminMode.liveState.value!!) {
                when (AdminMode.mode) {
                    //열기 모드
                    (1) -> {
                        //TODO OPEN
                        MyBluetoothService.g_socket?.let {
                            MyBluetoothService().ConnectedThread(it, getSelectedId()).run()
                        }
                        //사용가능
                        if(node == null){
                            Toast.makeText(context,"${getSelectedId()}번이 열렸습니다.",Toast.LENGTH_SHORT).show()
                            AdminMode.liveState.value = false
                        }
                        else{
                            //사용 중
                            if(node.enabled){
                                val openDialog = OpenDialog(context)
                                openDialog.show()
                                AdminMode.liveState.value = false
                            }
                            //점검 중
                            else{
                                Toast.makeText(context,"${getSelectedId()}번이 열렸습니다.",Toast.LENGTH_SHORT).show()
                                AdminMode.liveState.value = false
                            }
                        }
                    }
                    //점검 중 모드
                    (2) -> {
                        //사용 가능
                        if(node == null){
                            liveNode.insert(NodeModel(getSelectedId(),"000000",false, null))
                            AdminMode.liveState.value = false
                        }

                        else{
                            //사용 중
                            if(node.enabled){
                                Toast.makeText(context,"사용중인 사물함 입니다. 반납후 설정해 주세요",Toast.LENGTH_SHORT).show()
                                AdminMode.liveState.value = false
                            }
                            //점검 중
                            else{
                                Toast.makeText(context,"점검 모드가 해제 되었습니다.",Toast.LENGTH_SHORT).show()
                                liveNode.delete(getSelectedId())
                                AdminMode.liveState.value = false
                            }
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
                else {
                    //사용 중
                    if(node.enabled){
                        Using().nodeClick(context, getSelectedId())
                    }
                    //점검 중
                    else{
                        Fixing().nodeClick(context, getSelectedId())
                    }
                }
            }
        }
    }
}