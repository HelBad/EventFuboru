package com.example.eventfuboru.api

import com.example.eventfuboru.model.DataScan
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitAPI {
    @Headers(
        "Content-Type: application/json",
        "X-Token: bd78cb96df3de3e068e22643760e85bbd9a66b3b6ec6b9248d580d011e489143"
    )
    @POST("manual-check-in")
    fun postData(@Body dataModal: DataScan?): Call<DataScan?>?
}