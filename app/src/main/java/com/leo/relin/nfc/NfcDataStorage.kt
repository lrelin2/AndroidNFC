package com.leo.relin.nfc

import android.content.Context
import android.content.SharedPreferences

object NfcDataStorage {
    private const val PREFS_NAME = "nfc_prefs"

    fun saveNfcData(context: Context, name: String, data: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(name, data).apply()
    }

    fun getAllNfcData(context: Context): Map<String, *> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.all
    }

    fun getSelectedData(context: Context, name: String): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(name, null)
    }
}