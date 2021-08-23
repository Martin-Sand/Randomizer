package com.example.randy

data class LeaderboardModel(val name: String, val wins: Int, val ranking: Int, var isChecked: Boolean = false)