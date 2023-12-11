package uk.ac.tees.w9591610.bitesearch.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import uk.ac.tees.w9591610.bitesearch.model.MealsRepository
import uk.ac.tees.w9591610.bitesearch.model.response.Category

class MealDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,

) : ViewModel() {
    private val repository: MealsRepository= MealsRepository.getInstance()
    var mealState = mutableStateOf<Category?>(null)

    init {
        val mealID = savedStateHandle.get<String>("meal_category_id")?:""
        mealState.value = repository.getMeal(mealID)

    }


}