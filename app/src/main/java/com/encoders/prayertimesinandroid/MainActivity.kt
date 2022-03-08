package com.encoders.prayertimesinandroid

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.anushka.retrofitdemo.APIInterface
import com.anushka.retrofitdemo.RetrofitInstanceClient
import com.encoders.prayertimesinandroid.Models.PrayerTimes
import com.encoders.prayertimesinandroid.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)

        binding.currentDay.text = "Current Date: $formatted"


        Prayer_Timers()
    }

    private fun Prayer_Timers() {

        binding.progressbar.visibility = View.VISIBLE
        val service = RetrofitInstanceClient.getRetrofitInstance()
            .create(APIInterface::class.java)
        val call = service.PRAYER_TIMES("pakistan"
        )
        call.enqueue(object : Callback<PrayerTimes> {
            override fun onResponse(
                call: Call<PrayerTimes>,
                response: Response<PrayerTimes>
            ) {
                if (response.code() == 200) {
                    binding.progressbar.visibility = View.GONE
                    val pryaers_time = response.body()!!
                    if (pryaers_time.code == 200) {
                        val now: Calendar = Calendar.getInstance()
                        val date = now.get(Calendar.DATE)

                        binding.fajar.text = pryaers_time.data[date].timings.Fajr
                        binding.duhur.text = pryaers_time.data[date].timings.Dhuhr
                        binding.asar.text = pryaers_time.data[date].timings.Asr
                        binding.magrib.text = pryaers_time.data[date].timings.Maghrib
                        binding.isha.text = pryaers_time.data[date].timings.Isha

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.error_occur),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }

            override fun onFailure(call: Call<PrayerTimes>, t: Throwable) {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_occur),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}