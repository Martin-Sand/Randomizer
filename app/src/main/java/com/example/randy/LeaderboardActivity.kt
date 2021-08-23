package com.example.randy

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var leaderboardAdapter: LeaderboardAdapter


    private fun populateView(): ArrayList<LeaderboardModel> {
        val dataBaseHandler = DataBaseHandler(this)
        val list: ArrayList<LeaderboardModel>  = dataBaseHandler.getLeaderboard()

        val iterator = list.listIterator()

        for(leader in iterator){
            leaderboardAdapter.addLeader(leader)
        }

        return list
    }

    private fun deleteSelected(list: ArrayList<LeaderboardModel>) {
        val iterator = list.listIterator()
        val dataBaseHandler = DataBaseHandler(this)
        for(item in iterator){
            if(item.isChecked) {
                dataBaseHandler.deleteName(item.name)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        leaderboardAdapter = LeaderboardAdapter(mutableListOf())

        supportActionBar?.hide()

        rvLeaderboardItems.adapter = leaderboardAdapter
        rvLeaderboardItems.layoutManager = LinearLayoutManager(this)

        val list = populateView()

        deleteSelectedBtn.setOnClickListener {
            deleteSelected(list)
            finish();
            startActivity(getIntent());
        }


    }
}