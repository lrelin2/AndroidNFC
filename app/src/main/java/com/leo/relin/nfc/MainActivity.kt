package com.leo.relin.nfc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReceive.setOnClickListener {
            startActivity(Intent(this, ReceiveActivity::class.java))
        }

        binding.btnTransmit.setOnClickListener {
            startActivity(Intent(this, TransmitActivity::class.java))
        }
    }
}