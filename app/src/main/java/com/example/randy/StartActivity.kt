package com.example.randy


import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var spinner: Spinner
    lateinit var players: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        spinner = findViewById(R.id.dropdownMenu)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.statsOptions,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        val initialSelectedPosition: Int = spinner.getSelectedItemPosition()
        spinner.setSelection(0) //clear selection

        spinner.onItemSelectedListener = this

        supportActionBar?.hide()

        animateView(pallenView)

        populatePodium()

        players = nPlayers.text.toString()
        if(players.equals("")){
            players = "0"
        }

        Start.setOnClickListener {
            val id = addRecord()
            val intent = Intent(this@StartActivity, RandomActivity::class.java)

            intent.putExtra("ID", id)
            startActivity(intent)
        }

        minusBtn.setOnClickListener {


            if(players.toInt() > 1) {
                players = (players.toInt() - 1).toString()
                nPlayers.setText(players)
            }
        }

        plusBtn.setOnClickListener{
            if(players.toInt() < 30){
                players = (players.toInt() + 1).toString()
                nPlayers.setText(players)
            }
        }
    }

    fun addRecord(): Int{
        val dataBaseHandler = DataBaseHandler(this)

        val min = if(low.text.toString().isEmpty()){
            0
        } else {
            Integer.parseInt(low.text.toString())
        }

        val max = if(high.text.toString().isEmpty()){
            100
        } else {
            Integer.parseInt(high.text.toString())
        }

        val players = if(nPlayers.text.toString().isEmpty()){
            0
        } else {
            Integer.parseInt(nPlayers.text.toString())
        }

        val status = dataBaseHandler.addRound(RoundModelClass(0, max, min, players, ""))

        /*
        if(status > -1) {
            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
        }
         */

        return status.toInt()

    }

    private fun animateView(view: ImageView) {
        when (val drawable = view.drawable) {
            is AnimatedVectorDrawableCompat -> {
                drawable.start()
            }
            is AnimatedVectorDrawable -> {
                drawable.start()
            }
        }
    }

    fun populatePodium(){
        val databaseHandler = DataBaseHandler(this)
        val list = databaseHandler.getLeaderboard()

        val n1 = list.getOrNull(0)
        val n2 = list.getOrNull(1)
        val n3 = list.getOrNull(2)

        if (n1 != null) {
                number1.text = n1.name
        }
        if (n2 != null) {
            number2.text = n2.name
        }
        if (n3 != null) {
            number3.text = n3.name
        }
    }

    override fun onRestart() {
        super.onRestart()
        populatePodium()
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var intent: Intent?
        when(spinner.selectedItemPosition){
            1 -> intent = Intent(this@StartActivity, StatsActivity::class.java)
            2 -> intent = Intent(this@StartActivity, LeaderboardActivity::class.java)
            else -> intent = null
        }
        Log.d(TAG, spinner.selectedItemPosition.toString())

        if (intent != null) {
            startActivity(intent)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}