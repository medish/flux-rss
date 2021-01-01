package com.example.projet_android.entities

import androidx.room.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Entity(foreignKeys = [ForeignKey(entity = Flux::class, parentColumns = ["id"] , childColumns = ["fluxid"],deferred = true, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)])
data class Info(
    @PrimaryKey(autoGenerate =true)
    var id: Long,
    var title: String,
    var description:String,
    var link: String,
    var nouveau:Boolean,
    var fluxid: Long,
    @TypeConverters(DateConverter::class)
    var pubDate : Date
){
    constructor(title: String, description: String, link: String, nouveau: Boolean, fluxid: Long, pubDate: Date) :
            this(0, title, description, link, nouveau, fluxid, pubDate)


}

  class DateConverter{
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") as DateFormat
        private val rssFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z") as DateFormat

    }
      @TypeConverter
      fun toDate(value : String?) : Date? {
          return value?.let {
              return try {
                  dateFormat.parse(it)
              } catch (e : ParseException){
                  null
              }
          }
      }
      @TypeConverter
      fun toTimestamp(date : Date?) : String?{

          return date?.toString()
      }

      fun rssDateToFormat(value : String) : String {
          val rssDate = rssFormat.parse(value) ?: Date()

          return dateFormat.format(rssDate)
      }

      fun dateToFormat(date : Date) : String{
          return dateFormat.format(date)
      }
}
