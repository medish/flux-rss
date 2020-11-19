package com.example.projet_android

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertFlux(vararg flux: Flux ) : List<Long>

    @Query("SELECT * FROM Flux")
    fun loadAllFlux(): LiveData<List<Flux>>

    @Query("SELECT * FROM Flux")
    fun loadAllFluxs(): List<Flux>


    @Query("SELECT * FROM Flux WHERE source= :nom")
    fun loadFlux(nom:String): LiveData<List<Flux>>


    @Query("DELETE  FROM Flux WHERE source = :nom")
    fun DeleteFlux(nom:String)

}

