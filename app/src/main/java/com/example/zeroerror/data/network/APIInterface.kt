package com.example.zeroerror.data.network

import com.example.zeroerror.data.model.Inspect
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("/inspections/{inspectId}")
    fun getInspectItem(@Path("inspectId")inspectId: Long): Call<Inspect>

    @FormUrlEncoded
    @PATCH("/inspections/{inspectId}/finish")
    fun patchInspectFinish(@Path("inspectId")inspectId: Long, @Field("completeYN")completeYN:Boolean): Call<Boolean>
}