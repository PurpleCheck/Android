package com.example.zeroerror.data.network

import com.example.zeroerror.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL

        val retrofit = Retrofit.Builder()
            .baseUrl(this.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val orderAPI: APIInterface = retrofit.create(APIInterface::class.java)
    }
}