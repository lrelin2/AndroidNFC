package com.leo.relin.nfc

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityTransmitBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransmitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransmitBinding
    private var savedTags: Map<String, *> = emptyMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransmitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            loadSavedData()

            binding.btnTransmitSignal.setOnClickListener {
                val selectedName = binding.spinnerSavedData.selectedItem as? String
                if (selectedName != null) {
                    val data = NfcDataStorage.getSelectedData(this, selectedName)
                    if (data != null) {
                        MyHostApduService.dataToTransmit = data
                        binding.tvTransmitStatus.text = getString(R.string.transmitting, selectedName)
                        addLog("HCE data set to: $data (Tag: $selectedName)")
                    } else {
                        addLog("Error: Could not retrieve data for $selectedName")
                    }
                } else {
                    addLog("Error: No tag selected")
                }
            }
            
            addLog("Transmit state initialized")
        } catch (e: Exception) {
            addLog("Error in onCreate: ${e.message}")
        }
    }

    private fun loadSavedData() {
        try {
            savedTags = NfcDataStorage.getAllNfcData(this)
            val names = savedTags.keys.toList()
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSavedData.adapter = adapter
            addLog("Loaded ${names.size} saved tags")
        } catch (e: Exception) {
            addLog("Error loading data: ${e.message}")
        }
    }

    private fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val logLine = "[$timestamp] $message\n"
        runOnUiThread {
            binding.tvLogs.append(logLine)
            binding.logScrollView.post {
                binding.logScrollView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }
}