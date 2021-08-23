package com.example.randy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_stats.*

class SelectStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_stats)

        supportActionBar?.hide()

        allRoundsBtn.setOnClickListener {
            val intent = Intent(this@SelectStatsActivity, StatsActivity::class.java)
            startActivity(intent)
        }

        leaderboardsBtn.setOnClickListener {
            val intent = Intent(this@SelectStatsActivity, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }
}