package com.example.randy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_leaderboard.view.*
import kotlinx.android.synthetic.main.item_stats.view.*

class StatsAdapter(
    private val stats: MutableList<RoundModelClass>

) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        return StatsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_stats,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val curStats = stats[position]
        holder.itemView.apply{
            idText.text = curStats.id.toString()
            maxText.text = curStats.max.toString()
            minText.text = curStats.min.toString()
            playersText.text = curStats.players.toString()
            winnerText.text = curStats.winner
            checked.setOnCheckedChangeListener { _ , isChecked ->
                curStats.isChecked = !curStats.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    fun addStats(stat: RoundModelClass){
        stats.add(stat)
        notifyItemInserted(stats.size - 1)
    }


}