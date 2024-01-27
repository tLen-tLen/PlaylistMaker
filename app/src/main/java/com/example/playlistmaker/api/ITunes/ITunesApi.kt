package com.example.playlistmaker.api.ITunes

import com.example.playlistmaker.dataclasses.ITunesTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesTrackResponse>
}