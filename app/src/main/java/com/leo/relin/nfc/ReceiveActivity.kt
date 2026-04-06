package com.leo.relin.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.leo.relin.nfc.databinding.ActivityReceiveBinding

class ReceiveActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private lateinit var binding: ActivityReceiveBinding
    private var nfcAdapter: NfcAdapter? = null
    private var lastCapturedData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val data = lastCapturedData
            if (name.isNotEmpty() && data != null) {
                NfcDataStorage.saveNfcData(this, name, data)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or
            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        val id = tag?.id?.joinToString("") { "%02x".format(it) } ?: "Unknown"
        lastCapturedData = id
        
        runOnUiThread {
            binding.tvStatus.text = getString(R.string.tag_captured)
            binding.tvData.text = getString(R.string.tag_id, id)
            binding.etName.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }
    }
}