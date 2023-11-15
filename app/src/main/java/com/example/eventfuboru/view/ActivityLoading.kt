package com.example.eventfuboru.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.eventfuboru.R

class ActivityLoading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        loading()
    }

    private fun loading() {
        val backgrond = object : Thread() {
            override fun run() {
                try {
                    sleep(2500)
                    startActivity(Intent(applicationContext, ActivityBeranda::class.java))
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        backgrond.start()
    }

    override fun onBackPressed() {}
}