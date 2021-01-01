package com.example.projet_android.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projet_android.entities.Info

@Dao
interface InfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInfo(vararg info: Info) : List<Long>

    @Query("SELECT * FROM Info")
    fun loadAllLiveInfos(): LiveData<List<Info>>

    @Query("SELECT * FROM Info")
    fun loadAllInfos(): List<Info>

    @Query("SELECT * FROM Info WHERE nouveau = :etat")
    fun loadNouveauInfo(etat: Boolean): List<Info>

    @Query("DELETE  FROM Info WHERE id = :id")
    fun deleteInfo(id: Long)

    @Query("UPDATE Info SET nouveau = 0 WHERE id = :id")
    fun change_etat(id:Long)

    @Query("SELECT * FROM Info WHERE title LIKE :s")
    fun recherche(s:String): List<Info>

}