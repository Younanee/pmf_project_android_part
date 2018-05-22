package mju_avengers.please_my_fridge.data


data class TestFoodData(var thumbnail : Int, var name: String, var percent : Int)
data class RecipeData(val index : Int, val content : String)


data class FoodData(var id : String,
                    var urls: ArrayList<String>,
                    var title: String,
                    var percent: Int,
                    var starRate : Float,
                    var ingredients : ArrayList<String>,
                    var directions : ArrayList<String>)

data class SimpleFoodData(var id : String,
                          var url : String,
                          var title : String,
                          var percent: Int,
                          var starRate : Float)

data class foodInfoData(
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