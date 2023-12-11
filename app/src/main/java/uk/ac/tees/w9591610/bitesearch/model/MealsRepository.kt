package uk.ac.tees.w9591610.bitesearch.model

import uk.ac.tees.w9591610.bitesearch.api.MealsWebService
import uk.ac.tees.w9591610.bitesearch.model.response.Category
import uk.ac.tees.w9591610.bitesearch.model.response.MealsCategoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsRepository(private val webService: MealsWebService= MealsWebService()) {
    companion object{
        @Volatile
        private var instance:MealsRepository?=null

        fun getInstance()=instance?: synchronized(this){
            instance?:MealsRepository().also {
                instance=it
            }
        }

    }

    private var cacheMeals= listOf<Category>()

    suspend fun getMeals(): MealsCategoriesResponse {
        var response=webService.getMeals()
        cacheMeals=response.categories
        return response

    }

    fun getMeal(mealID:String):Category?{
        return cacheMeals.firstOrNull {
            it.idCategory==mealID
        }
    }
}