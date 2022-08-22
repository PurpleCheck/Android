package com.example.zeroerror.data.network

import com.example.zeroerror.data.model.FinishResponse
import com.example.zeroerror.data.model.Inspect
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("/inspections/{inspectId}")
    fun getInspectItem(@Path("inspectId")inspectId: Long): Call<Inspect>

    @PATCH("/inspections/{inspectId}/finish")
    fun patchInspectFinish(@Path("inspectId")inspectId: Long, @Body params: HashMap<String, Boolean>): Call<FinishResponse>
}