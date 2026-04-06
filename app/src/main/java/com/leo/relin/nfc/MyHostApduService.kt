package com.leo.relin.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHostApduService : HostApduService() {

    companion object {
        @Volatile
        var dataToTransmit: String? = null
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return byteArrayOf(0x6A, 0x82.toByte())

        // Check for SELECT AID command (starts with 00 A4 04 00)
        if (commandApdu.size >= 4 && commandApdu[0] == 0x00.toByte() && commandApdu[1] == 0xA4.toByte()) {
            Log.d("HCE", "AID Selected")
            return byteArrayOf(0x90.toByte(), 0x00.toByte()) // Success
        }

        val hexData = dataToTransmit
        if (hexData != null) {
            try {
                val response = hexStringToByteArray(hexData)
                // Append 90 00 (Success status words)
                return response + byteArrayOf(0x90.toByte(), 0x00.toByte())
            } catch (e: Exception) {
                Log.e("HCE", "Error parsing hex data", e)
            }
        }
        
        return byteArrayOf(0x6F, 0x00.toByte()) // Unknown error
    }

    override fun onDeactivated(reason: Int) {
        Log.d("HCE", "Deactivated: $reason")
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}