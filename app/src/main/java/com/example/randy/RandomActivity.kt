package com.example.randy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.activity_random.*
import kotlin.random.Random


fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}

class RandomActivity : AppCompatActivity() {

    private lateinit var numberAdapter: NumberAdapter

    private fun populateView() {
        val iterator = results.listIterator()

        for(n in iterator){
            val number = Number(n)
            numberAdapter.addNumber(number)
        }
    }

    private var n_rounds = 0
    private val results = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        numberAdapter = NumberAdapter(mutableListOf())

        supportActionBar?.hide()

        val lm = GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false)

        rvNumberItems.adapter = numberAdapter
        rvNumberItems.layoutManager = lm

        populateView()

        val dataBaseHandler = DataBaseHandler(this)

        val id: Int = intent.getIntExtra("ID", 0)

        val max = if(id != 0) dataBaseHandler.getRound(id.toString()).max else 100
        val min = if(id != 0) dataBaseHandler.getRound(id.toString()).min else 0
        val players = if(id != 0) dataBaseHandler.getRound(id.toString()).players else 0

        val round = RoundModelClass(id, max, min, players, "")

        dataBaseHandler.deleteAllNameless()

        if(players <= 1){
            counter.visibility = (View.INVISIBLE)
            dataBaseHandler.deleteRound(round.id)
        }
        counter.text = ("$n_rounds/$players")



        btnRandomize.setOnClickListener {

            //New randomize
            if(btnRandomize.text != "SAVE"){
                val n = rand(min, max)
                val num = Number(n)
                results.add(n)
                numberAdapter.addNumber(num)
                number.text = n.toString()
                n_rounds++
                counter.text = ("$n_rounds/$players")

                if(n_rounds == players ) {
                    winnerInput.visibility = (View.VISIBLE)
                    btnRandomize.text = ("SAVE")
                }
            }

            //Save winner-input
            else {
                round.winner = winnerInput.text.toString().uppercase()
                if(round.winner.isNotEmpty()){
                    dataBaseHandler.addRound(round)
                }

                val intent = Intent(this@RandomActivity, StartActivity::class.java)
                startActivity(intent)
                finish()
            }


        }

    }
}