package com.example.projet_android.models

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.projet_android.FluxData
import com.example.projet_android.dao.InfoDao
import com.example.projet_android.entities.Info

class InfoModel(application: Application) : AndroidViewModel(application) {
    private val infoDao : InfoDao by lazy { FluxData.getInstance(application).infoDao }
    val allInfos : LiveData<List<Info>> by lazy{ infoDao.loadAllLiveInfos() }


    fun allInfo():List<Info>{
        var lsinfo = emptyList<Info>()
        val tr = Thread{

            try{
                lsinfo = infoDao.loadAllInfos()}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return lsinfo
    }

    fun addInfo(i: Info): List<Long> {

        var ls = listOf<Long>()
        val tr = Thread{

            try{
                ls = infoDao.insertInfo(i)}
            catch(e : SQLiteConstraintException){
                Log.e("SQL_ERREUR",e.toString())
            }

        }
        tr.start()
        tr.join()
        return ls
    }
}