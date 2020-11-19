package com.example.projet_android

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class InfoModel(application: Application) : AndroidViewModel(application) {
    val db : InfoData by lazy{ InfoData.getInstance( application )}

    val allInfos: LiveData<List<Info>> by lazy{ db.Daoinsert.loadAllInfo() }

    var lsinfo = emptyList<Info>()

    fun allInfo():List<Info>{
        val tr = Thread{

            try{
                lsinfo = db.Daoinsert.loadAllInfos()}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return lsinfo
    }

    fun ajouterInfo(i: Info): List<Long> {

        var ls = listOf<Long>()
        val tr = Thread{

            try{
                ls = db.Daoinsert.insertInfo(i)}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return ls
    }
}