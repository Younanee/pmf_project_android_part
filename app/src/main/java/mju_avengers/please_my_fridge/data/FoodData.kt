package mju_avengers.please_my_fridge.data


data class TestFoodData(var thumbnail : Int, var name: String, var percent : Int)
data class FoodData(val _id : String, val name : String, val recipe: ArrayList<RecipeData>)
data class RecipeData(val index : Int, val content : String)