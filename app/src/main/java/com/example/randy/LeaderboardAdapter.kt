package com.example.randy

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_leaderboard.view.*

class LeaderboardAdapter(
    private val leaders: MutableList<LeaderboardModel>
): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        return LeaderboardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_leaderboard,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val curLeader = leaders[position]
        holder.itemView.apply{
            nameText.text = curLeader.name
            winsText.text = curLeader.wins.toString()
            rankingText.text = curLeader.ranking.toString()
            selected.setOnCheckedChangeListener { _ , isChecked ->
                curLeader.isChecked = !curLeader.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return leaders.size
    }

    fun addLeader(leader: LeaderboardModel){
        leaders.add(leader)
        notifyItemInserted(leaders.size - 1)
    }
}