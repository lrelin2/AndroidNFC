package com.leo.relin.nfc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            binding.btnReceive.setOnClickListener {
                addLog("Navigating to Receive State")
                startActivity(Intent(this, ReceiveActivity::class.java))
            }

            binding.btnTransmit.setOnClickListener {
                addLog("Navigating to Transmit State")
                startActivity(Intent(this, TransmitActivity::class.java))
            }
            
            addLog("Application Started")
        } catch (e: Exception) {
            addLog("Error in MainActivity: ${e.message}")
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