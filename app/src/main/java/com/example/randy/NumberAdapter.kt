package com.example.randy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_number.view.*




class NumberAdapter(
    private val list: MutableList<Number>
): RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberAdapter.NumberViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_number,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val curNum = list[position]
        holder.itemView.apply{
            numberText.text = curNum.value.toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addNumber(n: Number){
        list.add(n)
        notifyItemInserted(list.size - 1)
    }
}