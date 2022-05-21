package com.example.smartlocker.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.*


class MyBluetoothService{
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    companion object {
        //통신할 블루투스 기기
        private var mDevice: BluetoothDevice? = null
        //전역에서 사용하는 소켓
        var g_socket: BluetoothSocket? = null
    }
    
    //앱이 실행되면 블루투스를 초기화한다. 블루투스가 켜져있지 않으면 앱 실행 불가
    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        //현재 페어링된 블루투스 집합
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        //페어링된 블루투스 중 "HC-06"이라는 이름을 가진 기기를 찾아 mDevice에 저장한다.
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            if(deviceName == "HC-06"){
                mDevice = device
            }
        }
        //블루투스 사용이 가능하면 연결을 시도한다.
        if(bluetoothAdapter?.isEnabled == true){
            ConnectThread(mDevice).run()
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice?) : Thread() {
        //전달받은 hc-06모델의 소켓을 생성한다.
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"))
        }
        //아두이노 기기와 블루투스 연결 시도한다.
        override fun run() {
            // 연결하기 전, 블루투스 탐색을 멈춘다.
            bluetoothAdapter?.cancelDiscovery()
            //생성한 소켓을 통해 연결을 시도한다.
            mmSocket?.let { socket ->
                socket.connect()
                //만든 소켓을 전역소켓 변수에 담아 전역에서 사용가능하게 한다.
                g_socket = socket
                //연결을 하자마자 전체닫기한다.
                ConnectedThread(socket,0).run()
            }
        }
        //에러가 발생하면 소켓을 닫는다.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not close the client socket", e)
            }
        }
    }
    //실제 데이터를 통신할 수 있는 클래스. 생성된 소켓과 제어하고 싶은 사물함 번호를 전달받는다.
    inner class ConnectedThread(private val mmSocket: BluetoothSocket, private val id:Int) : Thread() {
        //데이터를 hc-06으로 보내기 위한 아웃풋 스트림
        private val mmOutStream: OutputStream = mmSocket.outputStream
        //스레드를 이용해 데이터를 보내면 사용자정의함수 write가 실행된다.
        override fun run() {
            write(id)
        }
        //사용자 정의함수: 아웃풋스트림을 이용해 해당 인자로 들어온 사물함을 제어한다.
        @SuppressLint("MissingPermission")
        fun write(id: Int) {
            //통신을 하기 위해서 사물함번호를 특정 문자로 변환한다.
            val byte: Char = switchToChar(id)
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    //데이터를 utf-16으로 인코딩해서 보낸다.
                    mmOutStream.write(byte.code)
                    //문이 열리면 5초동안 대기한다.
                    delay(5000)
                    //전체닫기면 해당함수를 종료한다.
                    if (id == 0) return@launch
                    //전체닫기가 아니면 해당사물함을 닫는다.
                    mmOutStream.write(byte.code-1)
                }

            } catch (e: IOException) {
                Log.d("myLog",e.toString())
                cancel()
            }
        }

        private fun switchToChar(id:Int):Char{
            return when(id){
                0-> '0' //전체 닫기
                1-> 'b' //1번 열기
                2-> 'd' //2번 열기
                3-> 'f' //3번 열기
                4-> 'h' //4번 열기
                5-> 'j' //5번 열기
                6-> '1' //전체 열기
                else -> {'0'}
            }
        }

        // Call this method from the main activity to shut down the connection.
        private fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {

            }
        }
    }







}