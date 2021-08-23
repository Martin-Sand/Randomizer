package com.example.randy

data class RoundModelClass(val id: Int, val max: Int, val min: Int, val players: Int, var winner: String, var isChecked: Boolean = false) : Comparable<RoundModelClass>{
    override fun compareTo(other: RoundModelClass): Int {
        return(winner.compareTo(other.winner))
    }

}
