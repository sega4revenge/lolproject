package com.sega.lolproject.services

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by sega4 on 16/12/2017.
 */

class SessionManager(private val context: Context) {


    // Shared Preferences
    private val pref: SharedPreferences

    // Editor for Shared preferences
    private val editor: SharedPreferences.Editor

    // Shared pref mode
    private val PRIVATE_MODE = 0

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun storeFullData(data: Boolean) {
        // Storing login value as TRUE
        editor.putBoolean(IS_FULLDATA, data)
        // commit changes
        editor.commit()
    }

    fun checkFullData(): Boolean {
        // Storing login value as TRUE
        return pref.getBoolean(IS_FULLDATA, false)
        // commit changes

    }

    fun storeOnlylData(data: Boolean) {
        // Storing login value as TRUE
        editor.putBoolean(IS_ONLYDATA, data)
        // commit changes
        editor.commit()
    }

    fun checkOnlyData(): Boolean {
        // Storing login value as TRUE
        return pref.getBoolean(IS_ONLYDATA, false)
        // commit changes

    }

    fun update_fulldata_champion(data: String) {
        // Storing login value as TRUE
        editor.putString(KEY_FULLDATA_CHAMPION, data)
        // commit changes
        editor.commit()
    }
    fun getResumeFull(): String {
        // Storing login value as TRUE
        return pref.getString(KEY_FULLDATA_CHAMPION,"Aatrox")
        // commit changes

    }
    fun update_onlydata_champion(data: String) {
        // Storing login value as TRUE
        editor.putString(KEY_ONLYDATA_CHAMPION, data)
        // commit changes
        editor.commit()
    }
    fun getResumeOnly(): String {
        // Storing login value as TRUE
        return pref.getString(KEY_ONLYDATA_CHAMPION,"Aatrox")
        // commit changes

    }
    companion object {

        // Sharedpref file name
        private val PREF_NAME = "lolproject"

        // All Shared Preferences Keys
        private val IS_FULLDATA = "fulldata"
        private val IS_ONLYDATA = "onlydata"
        // User name (make variable public to access from outside)
        val KEY_FULLDATA_CHAMPION = "fulldata_champion"

        // Email address (make variable public to access from outside)
        val KEY_ONLYDATA_CHAMPION = "onlydata_champion"
    }
}