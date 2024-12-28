package com.jdcoding.trafficlightsapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var redLight: ImageView
    private lateinit var yellowLight: ImageView
    private lateinit var greenLight: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var isMidnightBlinking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redLight = findViewById(R.id.redLight)
        yellowLight = findViewById(R.id.yellowLight)
        greenLight = findViewById(R.id.greenLight)

        startTrafficLightCycle()
    }

    private fun startTrafficLightCycle() {
        handler.postDelayed({ runTrafficLightCycle() }, 0)
    }

    private fun runTrafficLightCycle() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

        // Check for midnight exception
        if (currentHour in 0..5) {
            isMidnightBlinking = true
            blinkYellowLight()
        } else {
            isMidnightBlinking = false
            switchToRed()
        }
    }

    private fun switchToRed() {
        resetLights()
        redLight.setBackgroundResource(R.drawable.red)
        handler.postDelayed({ switchToYellow() }, 5000)
    }

    private fun switchToYellow() {
        resetLights()
        yellowLight.setBackgroundResource(R.drawable.orange)
        handler.postDelayed({ switchToGreen() }, 2000)
    }

    private fun switchToGreen() {
        resetLights()
        greenLight.setBackgroundResource(R.drawable.green)
        handler.postDelayed({ runTrafficLightCycle() }, 5000)
    }

    private fun blinkYellowLight() {
        if (isMidnightBlinking) {
            val currentBackground = yellowLight.background
            val onDrawable = ResourcesCompat.getDrawable(resources, R.drawable.orange, null)
            val offDrawable = ResourcesCompat.getDrawable(resources, R.drawable.off, null)
            yellowLight.setBackgroundResource(if (currentBackground.constantState == onDrawable?.constantState) {
                R.drawable.off
            } else {
                R.drawable.orange
            })
            handler.postDelayed({ blinkYellowLight() }, 500)
        }
    }

    private fun resetLights() {
        redLight.setBackgroundResource(R.drawable.off)
        yellowLight.setBackgroundResource(R.drawable.off)
        greenLight.setBackgroundResource(R.drawable.off)
    }
}
