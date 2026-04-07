package com.leo.relin.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityReceiveBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReceiveActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private lateinit var binding: ActivityReceiveBinding
    private var nfcAdapter: NfcAdapter? = null
    private var lastCapturedData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            if (nfcAdapter == null) {
                addLog("NFC is not supported on this device")
            } else if (!nfcAdapter!!.isEnabled) {
                addLog("NFC is disabled. Please enable it in settings.")
            } else {
                addLog("NFC initialized and ready")
            }

            binding.btnSave.setOnClickListener {
                val name = binding.etName.text.toString()
                val data = lastCapturedData
                if (name.isNotEmpty() && data != null) {
                    NfcDataStorage.saveNfcData(this, name, data)
                    addLog("Saved tag: $name ($data)")
                    finish()
                } else {
                    addLog("Error: Name cannot be empty")
                }
            }
        } catch (e: Exception) {
            addLog("Error in onCreate: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            nfcAdapter?.enableReaderMode(this, this,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null)
            addLog("Reader mode enabled")
        } catch (e: Exception) {
            addLog("Error enabling reader mode: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
        addLog("Reader mode disabled")
    }

    override fun onTagDiscovered(tag: Tag?) {
        try {
            val id = tag?.id?.joinToString("") { "%02x".format(it) } ?: "Unknown"
            lastCapturedData = id
            addLog("Tag Discovered! ID: $id")
            
            runOnUiThread {
                binding.tvStatus.text = getString(R.string.tag_captured)
                binding.tvData.text = getString(R.string.tag_id, id)
                binding.etName.visibility = View.VISIBLE
                binding.btnSave.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            addLog("Error processing tag: ${e.message}")
        }
    }

    private fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val logLine = "[$timestamp] $message\n"
        runOnUiThread {
            binding.tvLogs.append(logLine)
            binding.logScrollView.post {
                binding.logScrollView.fullScroll(android.view.View.FOCUS_DOWN)
            }
        }
    }
}