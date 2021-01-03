package com.example.projet_android.entities

import androidx.room.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Entity(foreignKeys = [ForeignKey(entity = Flux::class, parentColumns = ["id"] , childColumns = ["fluxid"],deferred = true,
    onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)]
    , indices = [Index( value = ["pubDate"], unique = true)])

data class Info(
    @PrimaryKey(autoGenerate =true)
    var id: Long,
    var title: String,
    var description:String,
    var link: String,
    var nouveau:Boolean,
    @ColumnInfo(index = true)
    var fluxid: Long,
    @TypeConverters(DateConverter::class)
    var pubDate : Date,
    var imageUrl : String,

    @Ignore
    var isConsulted : Boolean = false
){
    constructor(title: String, description: String, link: String, nouveau: Boolean, fluxid: Long, pubDate: Date, imageUrl: String) :
            this(0, title, description, link, nouveau, fluxid, pubDate, imageUrl)


}

  class DateConverter{
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") as DateFormat
        private val rssFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z") as DateFormat
        private val outPutFormat = SimpleDateFormat("EEEE, d MMM yyyy HH:mm", Locale.getDefault()) as DateFormat

    }
      @TypeConverter
      fun toDate(value : String?) : Date? {
          return value?.let {
              return dateFormat.parse(value)
          }
      }
      @TypeConverter
      fun toTimestamp(date : Date?) : String?{

          return date?.let {
              return dateFormat.format(it)
          }
      }

      fun rssDateToFormat(value : String) : Date {
          return try {
              rssFormat.parse(value)!!
          }catch (e : ParseException){
              Date()
          }
      }

      fun dateToFormat(date : Date) : String{
          return dateFormat.format(date)
      }

      fun dateToOutputFormat(date : Date) : String{
          return outPutFormat.format(date)
      }
}
