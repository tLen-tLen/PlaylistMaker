package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.ITunesTrackSearchRequest
import com.example.playlistmaker.search.data.dto.Response

class RetrofitNetworkClient(private val itunesService: ITunesApi): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is ITunesTrackSearchRequest) {
            val resp = itunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}