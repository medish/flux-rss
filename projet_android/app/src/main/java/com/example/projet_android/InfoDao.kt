package com.example.projet_android

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InfoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertInfo(vararg info: Info ) : List<Long>

    @Query("SELECT * FROM Info")
    fun loadAllInfo(): LiveData<List<Info>>

    @Query("SELECT * FROM Info")
    fun loadAllInfos(): List<Info>


    @Query("SELECT * FROM Info WHERE nouveau ")
    fun loadNouveauInfo(): LiveData<List<Info>>


    @Query("DELETE  FROM Info WHERE title = :nom")
    fun DeleteInfo(nom:String)

}