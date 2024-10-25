package com.dicoding.myfinalsubmission2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    fun getEvent (@Query("active") active: String): Call<Event>

    @GET("events/{id}")
    fun getDetailEvent (@Path("id") id: String): Call<EventDetail>
}
