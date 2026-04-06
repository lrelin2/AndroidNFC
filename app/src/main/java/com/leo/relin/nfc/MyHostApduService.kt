package com.leo.relin.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class MyHostApduService : HostApduService() {

    companion object {
        var dataToTransmit: String? = null
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        // This is a very basic implementation. 
        // Real-world NFC emulation is complex and depends on the protocol.
        // Here we just return the stored data if any, or a default response.
        
        return dataToTransmit?.toByteArray() ?: byteArrayOf(0x00, 0x00)
    }

    override fun onDeactivated(reason: Int) {
        // Handle deactivation
    }
}