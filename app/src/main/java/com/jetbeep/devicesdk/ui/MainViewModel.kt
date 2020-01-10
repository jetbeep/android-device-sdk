package com.jetbeep.devicesdk.ui

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.jetbeep.merchantsdk.MerchantSdk
import com.jetbeep.merchantsdk.gatt.BleDeviceApi
import org.koin.core.inject

class MainViewModel : BaseViewModel() {

    companion object {
        const val TAG = "JB_MainViewModel"
    }

    private val model: BleDeviceApi by inject()

    val connecting = MutableLiveData<Boolean>()
    val connected = MutableLiveData<Boolean>()
    val visibleDevicesId = MutableLiveData<List<Int>>()

    private val listener = object : BleDeviceApi.DeviceApiListener() {
        override fun onConnected(bluetoothDevice: BluetoothDevice?) {
            connecting.value = false
            connected.value = true
        }

        override fun onError(ex: Exception) {
            connecting.value = false
        }

        override fun onDisconnected(bluetoothDevice: BluetoothDevice?) {
            connecting.value = false
            connected.value = false
        }

        override fun onDetectedDevices(devices: List<Int>) {
            visibleDevicesId.value = devices
        }

        override fun onAdvertiseError(ex: Exception) {
            connecting.value = false
            connected.value = false
        }
    }

    init {
        Log.d(TAG, "Init Connect view model $this")

        connected.value = model.isConnected()
        model.subscribe(listener)
    }

    fun connect(merchantToken: String, deviceId: String) {
        if (connecting.value != true) {
            connecting.value = true

            model.connect(merchantToken, deviceId, MainActivity::class.java)
        }
    }

    fun cancel() {
        model.disconnect()
        connecting.value = false
    }

    fun startScanDevices() {
        model.startScanDevices()
    }

    fun stopScanDevices() {
        model.stopScanDevices()
    }

    override fun removeObservers(owner: LifecycleOwner) {
        Log.d(TAG, "removeObservers")
        model.stopScanDevices()
        model.unsubscribe(listener)

        connected.removeObservers(owner)
        connecting.removeObservers(owner)
    }
}