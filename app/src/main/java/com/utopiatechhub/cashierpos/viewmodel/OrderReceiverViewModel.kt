package com.utopiatechhub.cashierpos.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class OrderReceiverViewModel(application: Application) : AndroidViewModel(application) {
    private val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val _receivedOrder = MutableLiveData<String>()
    val receivedOrder: LiveData<String> = _receivedOrder

    private var serverSocket: BluetoothServerSocket? = null

    fun startListening() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord("FoodOrderApp", uuid)

                val socket = serverSocket?.accept()
                val inputStream = socket?.inputStream
                val buffer = ByteArray(1024)
                val bytesRead = inputStream?.read(buffer) ?: 0
                val receivedData = String(buffer, 0, bytesRead)

                _receivedOrder.postValue(receivedData)
                socket?.close()
            } catch (e: SecurityException) {
                _receivedOrder.postValue("Permission denied: ${e.message}")
            } catch (e: Exception) {
                _receivedOrder.postValue("Error receiving order: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serverSocket?.close()
    }
}

