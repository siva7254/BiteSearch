package uk.ac.tees.w9591610.bitesearch.model.response


data class MealsCategoriesResponse(
    val categories: List<Category>,
)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
)