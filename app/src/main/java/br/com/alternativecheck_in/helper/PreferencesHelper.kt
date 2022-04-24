package br.com.alternativecheck_in.helper

import android.content.Context
import br.com.alternativecheck_in.R

class PreferencesHelper private constructor(context: Context) {

    companion object {
        fun getInstance(context: Context): PreferencesHelper {
            return PreferencesHelper(context)
        }
    }
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
    private val edit = sharedPref.edit()

    fun setPreferencesBoolean(key: String, value: Boolean) {
        edit.putBoolean(key, value).apply()
    }
    fun getPreferencesBoolean(key: String, value: Boolean): Boolean {
        return sharedPref.getBoolean(key, value)
    }
}