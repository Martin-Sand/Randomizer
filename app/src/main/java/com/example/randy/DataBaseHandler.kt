package com.example.randy

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.collections.ArrayList

class DataBaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "RandomizerDB"
        private const val TABLE_CONTACTS = "RoundsTable"
        private const val TABLE_NAMES = "NamesTable"

        //Round Table column names
        private const val KEY_ID = "_id"
        private const val KEY_MAX = "max"
        private const val KEY_MIN = "min"
        private const val KEY_PLAYERS = "players"
        private const val KEY_WINNER = "winner"

        //Names columns names
        private const val KEY_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase?){
        //Lager tabell
        val CREATE_ROUNDS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_MAX + " INTEGER," +
                KEY_MIN + " INTEGER," +
                KEY_PLAYERS + " INTEGER," +
                KEY_WINNER + " TEXT" + ")")

        db?.execSQL(CREATE_ROUNDS_TABLE)

        val CREATE_NAMES_TABLE = ("CREATE TABLE " + TABLE_NAMES + "(" +
                KEY_NAME + " TEXT PRIMARY KEY)")

        db?.execSQL(CREATE_NAMES_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAMES")

        onCreate(db)
    }

    //Round Table methods

    fun addRound(round: RoundModelClass) : Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_MAX, round.max)
        contentValues.put(KEY_MIN, round.min)
        contentValues.put(KEY_PLAYERS, round.players)
        contentValues.put(KEY_WINNER, round.winner)

        //Setter inn rad
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close()
        return success


    }

    fun getAllRounds(): ArrayList<RoundModelClass> {
        val roundList: ArrayList<RoundModelClass> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var max: Int
        var min: Int
        var players: Int
        var winner: String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                max = cursor.getInt(cursor.getColumnIndex(KEY_MAX))
                min = cursor.getInt(cursor.getColumnIndex(KEY_MIN))
                players = cursor.getInt(cursor.getColumnIndex(KEY_PLAYERS))
                winner = cursor.getString(cursor.getColumnIndex(KEY_WINNER))

                val round = RoundModelClass(id, max, min, players, winner)
                roundList.add(round)
            } while(cursor.moveToNext())
        }

        cursor.close()
        db.close()


        return roundList
    }

    fun deleteAllNameless(): Int{
        val db = this.writableDatabase

        //Sletter rader
        val success = db.delete(TABLE_CONTACTS, "$KEY_WINNER = ''", null)

        db.close()
        return success
    }

    fun updateRound(round: RoundModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_MAX, round.max)
        contentValues.put(KEY_MIN, round.min)
        contentValues.put(KEY_PLAYERS, round.players)
        contentValues.put(KEY_WINNER, round.winner)

        //Oppdaterer rad
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + " = " + round.id, null)


        db.close()


        return success
    }

    fun deleteRound(round: Int): Int {
        val db = this.writableDatabase

        //SLetter rad
        val success = db.delete(TABLE_CONTACTS, "$KEY_ID = $round", null)

        db.close()
        return success
    }

    fun deleteName(name: String): Int {
        val db = this.writableDatabase

        //SLetter rad
        val success = db.delete(TABLE_CONTACTS, "$KEY_WINNER = '$name'", null)
        db.close()

        return success
    }

    fun getRound(ID: String): RoundModelClass {

        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS WHERE $KEY_ID = $ID"
        val cursor = db.rawQuery(selectQuery, null)



        val info = ArrayList<String>()
        try {
            Log.i(TAG, cursor.count.toString())
            if (cursor.getCount() != 0) {
                cursor.moveToFirst()
                if (cursor.getCount() > 0) {
                    do {
                        info.add(cursor.getString(cursor.getColumnIndex(KEY_ID)))
                        info.add(cursor.getString((cursor.getColumnIndex(KEY_MAX))))
                        info.add(cursor.getString((cursor.getColumnIndex(KEY_MIN))))
                        info.add(cursor.getString((cursor.getColumnIndex(KEY_PLAYERS))))
                        info.add(cursor.getString((cursor.getColumnIndex(KEY_WINNER))))
                    } while ((cursor.moveToNext()))
                }
            }
        } finally {
            cursor.close()
        }

        db.close()
        val round = RoundModelClass(info.get(0).toInt(), info.get(1).toInt(), info.get(2).toInt(), info.get(3).toInt(), info.get(4))


        return round
    }

    fun deleteAll(){
        val db = this.readableDatabase
        db.execSQL("delete from "+ TABLE_CONTACTS)
        db.close()
    }

    fun getLatestID(): Int {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS WHERE $KEY_ID=(SELECT max($KEY_ID) FROM $TABLE_CONTACTS)"
        val cursor = db.rawQuery(selectQuery, null)

        val info = ArrayList<Int>()
        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst()
                if (cursor.getCount() > 0) {
                    do {
                        info.add(cursor.getInt(cursor.getColumnIndex(KEY_ID)))
                    } while ((cursor.moveToNext()))
                }
            }
        } finally {
            cursor.close()
        }
        db.close()

        return info.get(0)
    }

    fun getLeaderboard() : ArrayList<LeaderboardModel> {
        val names_list: ArrayList<LeaderboardModel> = ArrayList()

        val selectQuery = "SELECT $KEY_WINNER, count($KEY_WINNER) as c FROM $TABLE_CONTACTS GROUP BY $KEY_WINNER ORDER BY c DESC"
        val db = this.readableDatabase
        var cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var name: String
        var count: Int
        var i = 1

        if(cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex(KEY_WINNER))
                count = cursor.getInt(cursor.getColumnIndex("c"))
                val name_model = LeaderboardModel(name, count, i)
                i++
                names_list.add(name_model)
            } while(cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return names_list
    }


}