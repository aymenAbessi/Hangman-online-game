package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class Hint : AppCompatActivity() {


    private val unsplashApiKey = "ZkS_Y5RJSJ6hXYGELw_3yarJPGVELoTWkZTYTOeFXZU"
    private val unsplashApi: UnsplashApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        unsplashApi = retrofit.create(UnsplashApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hint)

        val searchQuery = intent.getStringExtra("word")
        val imageView: ImageView = findViewById(R.id.hint)

        if (searchQuery != null) {
            unsplashApi.searchPhotos(searchQuery, unsplashApiKey)
                .enqueue(object : Callback<UnsplashResponse> {
                    override fun onResponse(
                        call: Call<UnsplashResponse>,
                        response: Response<UnsplashResponse>
                    ) {
                        if (response.isSuccessful) {
                            val photos = response.body()?.results
                            if (!photos.isNullOrEmpty()) {
                                val randomIndex = Random.nextInt(photos.size)
                                val imageUrl = photos[randomIndex].urls.regular
                                Picasso.get().load(imageUrl).into(imageView)
                            }

                        }
                    }
                    override fun onFailure(call: Call<UnsplashResponse>, t: Throwable) {

                    }
                })
        }
    }
}
