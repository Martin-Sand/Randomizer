package com.example.randy


import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : AppCompatActivity() {

    private lateinit var statsAdapter: StatsAdapter
    private val refreshPage = false

    fun populateView(): ArrayList<RoundModelClass>{
        val dataBaseHandler = DataBaseHandler(this)
        val list: ArrayList<RoundModelClass>  = dataBaseHandler.getAllRounds()


        val iterator = list.listIterator()

        for(round in iterator){
            statsAdapter.addStats(round)
        }
        return list
    }

    private fun deleteSelectedAndRefresh(list: ArrayList<RoundModelClass>) {
        val iterator = list.listIterator()
        val dataBaseHandler = DataBaseHandler(this)
        for(item in iterator){
            if(item.isChecked) {
                dataBaseHandler.deleteName(item.winner)
            }
        }
        finish()
        startActivity(getIntent())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        supportActionBar?.hide()

        statsAdapter = StatsAdapter(mutableListOf())

        rvStatsItems.adapter = statsAdapter
        rvStatsItems.layoutManager = LinearLayoutManager(this)

        val list = populateView()

        deleteAllBtn.setOnClickListener {
            deleteAllAndRefresh()
        }

        deleteSelectedRoundsBtn.setOnClickListener {
            deleteSelectedAndRefresh(list)
        }


    }

    private fun deleteAllAndRefresh(){
        val databaseHandler = DataBaseHandler(this)
        databaseHandler.deleteAll()
        finish()
        startActivity(getIntent())
    }

    private fun showAlertDialog(): Int{
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        var x: Int

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()
        return 0
    }


}