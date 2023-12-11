package uk.ac.tees.w9591610.bitesearch.api

import uk.ac.tees.w9591610.bitesearch.model.response.Category
import uk.ac.tees.w9591610.bitesearch.model.response.MealsCategoriesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class MealsWebService {

    private var mealsApi:MealsAppService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

         mealsApi = retrofit.create(MealsAppService::class.java)
    }


    interface MealsAppService {
        @GET("categories.php")
        suspend fun getMealsCategoriesApi(): MealsCategoriesResponse
    }


    //Api Call methods that wil be accessed from View models

    suspend fun getMeals():MealsCategoriesResponse{
        return mealsApi.getMealsCategoriesApi()
    }

}