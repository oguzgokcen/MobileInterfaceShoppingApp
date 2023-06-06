package com.example.letgocloneapp.requestUrl


import com.example.letgocloneapp.Models.ItemData
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
interface ItemService {

    @POST("addItemObject")
    fun addItem(@Body itemData: ItemData):Call<ItemData>

    @GET("getItems")
    fun getItems():Call<List<ItemData>>
}