package com.orhankoc_hw2

import retrofit2.Call
import retrofit2.http.GET



interface ApiService {
    @GET("6D8Z") // URL'nin sadece son kısmını yazıyoruz.
    fun getRestaurants(): Call<List<Restaurant>>
}
