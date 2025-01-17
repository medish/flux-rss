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

    @Query("SELECT * FROM Info ORDER BY nouveau DESC, datetime(pubDate) DESC")
    fun loadAllLiveInfos(): LiveData<List<Info>>

    @Query("SELECT * FROM Info ORDER BY nouveau DESC, datetime(pubDate) DESC")
    fun loadAllInfos(): List<Info>

    @Query("SELECT * FROM Info WHERE nouveau = :etat")
    fun loadNouveauInfo(etat: Boolean): List<Info>

    @Query("DELETE  FROM Info WHERE id = :id")
    fun deleteInfo(id: Long)

    @Query("UPDATE Info SET nouveau = 0 WHERE id = :id")
    fun changeEtatInfo(id:Long)

    @Query("Update Info SET nouveau = 0 WHERE id IN(:ids)")
    fun changeEtatInfo(ids : List<Long>)

    @Query("SELECT * FROM Info WHERE title LIKE :s OR description LIKE :s")
    fun recherche(s:String): List<Info>

    @Query("SELECT * FROM Info " +
            "WHERE (date(pubDate) BETWEEN date('now', :sortPubDate) AND date('now', 'localtime')) " +
            "AND nouveau like'%' || :sortNouveauFilter || '%' "+
            "ORDER BY " +
            "CASE WHEN :sortFilter IS NULL THEN pubDate END ASC," +
            "CASE WHEN :sortFilter IS NOT NULL THEN pubDate END DESC")
    fun filterQuery(sortFilter : String?, sortPubDate : String, sortNouveauFilter : String) : List<Info>
}