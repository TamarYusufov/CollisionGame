package com.example.a24c_10357hw1.model

import com.google.gson.Gson
import java.time.LocalDateTime

data class Game private constructor(
    val score: Int,
    val lat: Double,
    val lon: Double,
    val date: LocalDateTime

) { class Builder(
    private var score: Int = 0,
    private var lat: Double = 32.114869,
    private var lon: Double = 34.818031,
    private var date: LocalDateTime = LocalDateTime.now()
)

{
    fun score(score: Int)  = apply { this.score = score }
    fun lat(lat: Double) = apply { this.lat = lat}
    fun lon(lon: Double) = apply { this.lon = lon}
    fun date(date: LocalDateTime) = apply { this.date = date }

    fun build() = Game(score,lat, lon, date)

    fun ScoreToJson() :String {
        val gson = Gson()
        val scoreAsJson: String = gson.toJson(score)
        return scoreAsJson
    }


    fun DateToJson() :String {
        val gson = Gson()
        val dateAsJson: String = gson.toJson(date)
        return dateAsJson
    }


}

}
