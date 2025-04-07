package com.utopiatechhub.cashierpos.viewmodel

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class OrderSenderViewModel(application: Application) : AndroidViewModel(application) {
    private val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val _discoveredDevices = MutableLiveData<List<BluetoothDevice>>()
    val discoveredDevices: LiveData<List<BluetoothDevice>> = _discoveredDevices

    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> = _connectionStatus

    private val discoveredDevicesList = mutableListOf<BluetoothDevice>()

    private var bluetoothSocket: BluetoothSocket? = null

    // Check if required permissions are granted
    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasPermissions(): Boolean {
        val context = getApplication<Application>().applicationContext
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
    }

    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (!discoveredDevicesList.contains(it)) {
                            discoveredDevicesList.add(it)
                            _discoveredDevices.postValue(discoveredDevicesList.toList()) // Update LiveData
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    _connectionStatus.postValue("Discovery Finished")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startDiscovery(context: Context) {
        if (!hasPermissions()) {
            _connectionStatus.postValue("Permissions not granted")
            return
        }

        if (bluetoothAdapter == null) {
            _connectionStatus.postValue("Bluetooth not supported on this device")
            return
        }

        if (bluetoothAdapter.isEnabled) {
            _connectionStatus.postValue("Bluetooth is already enabled")
            return
        }

        if (bluetoothAdapter.isEnabled) {
            _connectionStatus.postValue("Bluetooth is not enabled")
            return
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)

        val filterFinished = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(discoveryReceiver, filterFinished)

        bluetoothAdapter.let { adapter ->
            try {
                // Check if discovery is already in progress
                if (adapter.isDiscovering) {
                    adapter.cancelDiscovery()
                }
                // Start discovery
                adapter.startDiscovery()
                _connectionStatus.postValue("Discovering Devices...")
            } catch (e: SecurityException) {
                _connectionStatus.postValue("Failed to start discovery: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun sendOrder(device: BluetoothDevice, waiterName: String, foodName: String, quantity: Int, totalPrice: Double, orderDate: String) {
        if (!hasPermissions()) {
            _connectionStatus.postValue("Permissions not granted")
            return
        }

        val orderJson = """
            {
                "waiterName": "$waiterName",
                "foodName": "$foodName",
                "quantity": $quantity,
                "totalPrice": $totalPrice,
                "orderDate": "$orderDate"
            }
        """.trimIndent()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uuid = device.uuids?.firstOrNull()?.uuid ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                bluetoothSocket?.connect()

                bluetoothSocket?.outputStream?.write(orderJson.toByteArray())
                _connectionStatus.postValue("Order sent successfully")
            } catch (e: SecurityException) {
                _connectionStatus.postValue("Failed to send order: Permission denied")
            } catch (e: Exception) {
                _connectionStatus.postValue("Failed to send order: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothSocket?.close()
    }
}

