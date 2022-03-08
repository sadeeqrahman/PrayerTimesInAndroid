package com.anushka.retrofitdemo


import com.encoders.prayertimesinandroid.Models.PrayerTimes
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {

     @GET("/v1/calendarByAddress")
    fun PRAYER_TIMES(
         @Query("address") address: String
    ):
            Call<PrayerTimes>



}