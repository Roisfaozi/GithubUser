package com.kotlin.githubuser.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.githubuser.R
import com.kotlin.githubuser.reciever.AlarmReciever
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val PREFS_NAME = "setting_pref"
        private const val DAILY = "daily"
    }

    private lateinit var alarmReciever: AlarmReciever
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)

        alarmReciever = AlarmReciever()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        change_language.setOnClickListener(this)

        tvDaily.isChecked = mSharedPreferences.getBoolean(DAILY, false)
        tvDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReciever.setAlarm(
                    applicationContext,
                    AlarmReciever.TYPE_REPEATING,
                    getString(R.string.daily_message)
                )
            } else{
                alarmReciever.cancelAlarm(applicationContext, AlarmReciever.TYPE_REPEATING)
            }
            saveSetting(isChecked)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    private fun saveSetting(value: Boolean){
        val mEdit = mSharedPreferences.edit()
        mEdit.putBoolean(DAILY, value)
        mEdit.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}