package mju_avengers.please_my_fridge.data


data class TestFoodData(var thumbnail : Int, var name: String, var percent : Int)
data class FoodData(val _id : String, val name : String, val recipe: ArrayList<RecipeData>)
data class RecipeData(val index : Int, val content : String)

data class HomeFoodData(var thumbnail: Int, var name: String, var percent: Int, var starRate : Float)

data class testRecipeData(
        val url : String,
        val directions : String,
        val categories : String,
        val ingredients : String,
        val title : String,
        val components : String,
        val calories : String,
        val carbohydrate : String,
        val sodium : String,
        val fat : String
)