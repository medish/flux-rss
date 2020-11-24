package com.example.projet_android.models

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projet_android.entities.Flux
import com.example.projet_android.FluxData

class AjouteFluxModel(application: Application) : AndroidViewModel(application) {
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

    fun ajouterFlux(f: Flux): List<Long> {

        var ls = listOf<Long>()
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
}