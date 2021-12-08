package com.example.bookfilter

import retrofit2.http.GET
import retrofit2.Call

interface HttpApiService {
    @GET("books")
    suspend fun getBooks() : List<Book>
}