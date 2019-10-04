package com.app.android.june.easyorder4u

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.messaging.FirebaseMessaging

class SettingsActivity : AppCompatActivity() {
    internal lateinit var switchCompat: SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.hide()
        switchCompat = findViewById(R.id.switch_toggle)
        Show()


    }

    private fun Show() {
            val sharedPreferences = getSharedPreferences("notify", Context.MODE_PRIVATE)
            val value = sharedPreferences.getInt("notifykey", 1)

            if (value == 1) {
                switchCompat.isChecked = true
                switchCompat.textOn = "On"
            } else {
                switchCompat.isChecked = false
                switchCompat.textOff = "Off"

            }
        }

        override fun onBackPressed() {
            super.onBackPressed()
        }

        fun cancel(view: View) {
            onBackPressed()
        }

        fun toggle(view: View) {
            val switchState = switchCompat.isChecked
            val SUBSCRIBE_TO = "userABC"
            if (switchState) {
                FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO)
                val sharedPreferences2 = getSharedPreferences("notify", Context.MODE_PRIVATE)
                val editor = sharedPreferences2.edit()
                editor.putInt("notifykey", 1)
                editor.apply()
                Toast.makeText(this, "On", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SUBSCRIBE_TO)
                val sharedPreferences2 = getSharedPreferences("notify", Context.MODE_PRIVATE)
                val editor = sharedPreferences2.edit()
                editor.putInt("notifykey", 0)
                editor.apply()
                Toast.makeText(this, "Off", Toast.LENGTH_SHORT).show()
            }
        }

    }

