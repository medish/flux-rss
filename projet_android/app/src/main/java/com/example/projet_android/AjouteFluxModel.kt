package com.example.projet_android

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class AjouteFluxModel(application: Application) : AndroidViewModel(application) {
    val db : FluxData by lazy{ FluxData.getInstance( application )}

    val allPays: LiveData<List<Flux>> by lazy{ db.Daoinsert.loadAllFlux() }

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