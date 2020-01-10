package com.jetbeep.devicesdk.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.jetbeep.devicesdk.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_ON_START_APP_BT = 123
    private val LOCATION_PERMISSIONS_REQUEST = 456
    private val DEFAULT_MERCHANT_TOKEN = "F09612A780C041D3939EE8C9CE8DC560"

    private val format = SimpleDateFormat("HH:mm:ss: ", Locale.getDefault())

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        console.movementMethod = ScrollingMovementMethod()
        printToConsole("Hello world")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSIONS_REQUEST
                )
            }
        }

        checkBluetooth()

        viewModel.visibleDevicesId.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                printToConsole("Visible devices: ${it.size}, id: ${it}")
            } else {
                printToConsole("No devices found :(")
            }
        })

        viewModel.connecting.observe(this, Observer {
            if (it == true) {
                printToConsole("Connecting...")
            }
        })

        viewModel.connected.observe(this, Observer {
            if (it == true) {
                printToConsole("Connected!")
            }
        })

        btnConnect.setOnClickListener {
            viewModel.startScanDevices()
            printToConsole("Started scanning for devices")
        }

        btnCancel.setOnClickListener {
            viewModel.stopScanDevices()
            printToConsole("Stopped scanning for devices")
            viewModel.cancel()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun printToConsole(text: String) {
        if (!isFinishing) {
            val oldText = console.text.toString()
            console.text = format.format(Date()) + text + "\n" + oldText
        }
    }

    private fun checkBluetooth(): Boolean {
        val bm = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bAdapter = bm.adapter

        if (bAdapter != null) {
            if (bAdapter.isEnabled) {
                return true
            }

            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_ON_START_APP_BT)
        } else {
            printToConsole("Error! Bluetooth adapter not found!")
        }
        return false
    }
}
