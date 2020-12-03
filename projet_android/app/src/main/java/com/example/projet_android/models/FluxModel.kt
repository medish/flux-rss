package com.example.projet_android.models

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projet_android.entities.Flux
import com.example.projet_android.FluxData
import com.example.projet_android.dao.FluxDao
import com.example.projet_android.entities.Info
import java.sql.SQLInput

class FluxModel(application: Application) : AndroidViewModel(application) {
    private val fluxDao : FluxDao by lazy{ FluxData.getInstance(application).fluxDao }

    val allfluxs: LiveData<List<Flux>> by lazy{ fluxDao.loadAllLiveFlux() }


    fun allflux():List<Flux>{
        var lsF = emptyList<Flux>()
        val tr = Thread{

            try{
                lsF = fluxDao.loadAllFluxs()}
            catch(e : SQLiteException){
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
                ls = fluxDao.insertFlux(f)}
            catch(e : SQLiteException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return ls
    }

    fun deleteFlux(id : Long) {
        val thread = Thread{
            try {
                fluxDao.deleteFlux(id)
            }catch (e : SQLiteException){
                Log.e("SQL_ERREUR",e.toString())
            }
        }
        thread.start()
    }
}