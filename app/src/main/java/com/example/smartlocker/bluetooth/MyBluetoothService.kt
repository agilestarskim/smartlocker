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
        private var mDevice: BluetoothDevice? = null
        var g_socket: BluetoothSocket? = null
    }

    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            if(deviceName == "HC-06"){
                mDevice = device
            }

        }
        if(bluetoothAdapter?.isEnabled == true){
            ConnectThread(mDevice).run()
        }

    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice?) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"))
        }


        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()
            mmSocket?.let { socket ->
                socket.connect()
                g_socket = socket
                //전체 닫기
                ConnectedThread(socket,0).run()
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not close the client socket", e)
            }
        }
    }

    inner class ConnectedThread(private val mmSocket: BluetoothSocket, private val id:Int) : Thread() {

        private val mmOutStream: OutputStream = mmSocket.outputStream
        override fun run() {
            write(id)
        }
        // Call this from the main activity to send data to the remote device.
        @SuppressLint("MissingPermission")
        fun write(id: Int) {
            val byte: Char = switchToChar(id)
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    mmOutStream.write(byte.code)
                    delay(5000)
                    if (id == 6 || id == 0) return@launch
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
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {

            }
        }
    }







}