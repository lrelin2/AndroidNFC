package com.leo.relin.nfc

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityTransmitBinding

class TransmitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransmitBinding
    private var savedTags: Map<String, *> = emptyMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransmitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSavedData()

        binding.btnTransmitSignal.setOnClickListener {
            val selectedName = binding.spinnerSavedData.selectedItem as? String
            if (selectedName != null) {
                val data = NfcDataStorage.getSelectedData(this, selectedName)
                if (data != null) {
                    MyHostApduService.dataToTransmit = data
                    binding.tvTransmitStatus.text = getString(R.string.transmitting, selectedName)
                }
            }
        }
    }

    private fun loadSavedData() {
        savedTags = NfcDataStorage.getAllNfcData(this)
        val names = savedTags.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSavedData.adapter = adapter
    }
}