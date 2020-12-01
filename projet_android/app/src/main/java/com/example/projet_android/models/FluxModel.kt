package com.example.projet_android.models

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projet_android.entities.Flux
import com.example.projet_android.FluxData
import com.example.projet_android.entities.Info

class FluxModel(application: Application) : AndroidViewModel(application) {
    val db : FluxData by lazy{ FluxData.getInstance(application) }

    val allfluxs: LiveData<List<Flux>> by lazy{ db.Daoinsert.loadAllFlux() }

    var lsF = emptyList<Flux>()

    fun allflux():List<Flux>{
        val tr = Thread{

            try{
                lsF = db.Daoinsert.loadAllFluxs()}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return lsF
    }

    fun ajouterFlux(f: Flux): Long {

        var ls : Long = -1
        val tr = Thread{

            try{
                ls = db.Daoinsert.insertFlux(f)}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return ls
    }

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