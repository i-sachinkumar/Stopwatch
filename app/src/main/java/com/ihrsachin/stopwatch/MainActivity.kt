package com.ihrsachin.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)
        val startButton = findViewById<ImageButton>(R.id.startButton)
        val resetButton = findViewById<ImageButton>(R.id.resetButton)
        val takeNoteButton = findViewById<ImageButton>(R.id.takeNoteButton)
        var isRunning = false
        var time: Long = 0
        var timeLaps: Long = 0
        var lapNo = 0
        val mHandler = Handler()
        var timeAtNoted : Long = 0
        val llt : LinearLayout = findViewById(R.id.linearUnderScroll)


        startButton.setOnClickListener {
            if (!isRunning) {
                resetButton.isClickable = false
                takeNoteButton.isClickable = true
                resetButton.alpha = .2f
                takeNoteButton.alpha = 1f
                startButton.setImageResource(android.R.drawable.ic_media_pause)
                val currentTime = System.nanoTime()
                runOnUiThread(object : Runnable {
                    override fun run() {
                        timeLaps = System.nanoTime() - currentTime
                        timeTextView.text = formatTime(timeLaps + time)
                        mHandler.postDelayed(this, 10)
                    }
                })
                isRunning = true
            }
            else{
                resetButton.isClickable = true
                takeNoteButton.isClickable = false
                resetButton.alpha = 1f
                takeNoteButton.alpha = .2f
                time += timeLaps
                timeTextView.text = formatTime(time)
                mHandler.removeCallbacksAndMessages(null)
                startButton.setImageResource(android.R.drawable.ic_media_play)
                isRunning = false
            }
        }

        resetButton.setOnClickListener {
            timeTextView.text = "00:00.00"
            isRunning = false
            time = 0
            timeLaps = 0
            lapNo = 0
            timeAtNoted = 0
            llt.removeAllViews()
            mHandler.removeCallbacksAndMessages(null)
        }

        takeNoteButton.setOnClickListener{
            lapNo++
            val v = LayoutInflater.from(this).inflate(R.layout.note_times,null)

            val lapNoTextView = v.findViewById<TextView>(R.id.lapCount)
            val elapsedTimeView = v.findViewById<TextView>(R.id.elapsedTime)
            val notedTimeView = v.findViewById<TextView>(R.id.notedTime)


            lapNoTextView.text = "Lap $lapNo"


            notedTimeView.text = formatTime(timeLaps+ time)
            elapsedTimeView.text = "Elapsed ${formatTime(time + timeLaps - timeAtNoted)}"
            timeAtNoted = time+timeLaps

            llt.addView(v,0)


        }





    }

    fun formatTime(timeLong: Long): String {

        val centiSecond: String =
            if ((timeLong / 10000000) % 100 < 10) "0${(timeLong / 10000000) % 100}"
            else "${(timeLong / 10000000) % 100}"
        val second: String =
            if ((timeLong / 1000000000) % 60 < 10) "0${(timeLong / 1000000000) % 60}"
            else "${(timeLong / 1000000000) % 60}"
        val minute: String =
            if ((timeLong / 60000000000) < 10) "0${(timeLong / 60000000000)}"
            else "${(timeLong / 60000000000)}"
        return "$minute:$second.$centiSecond"

    }

}

        

