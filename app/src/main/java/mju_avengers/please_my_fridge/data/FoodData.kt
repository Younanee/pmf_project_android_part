package mju_avengers.please_my_fridge.data

import java.io.Serializable

data class FoodData(var urls: ArrayList<String>,
                    var directions : ArrayList<String>,
                    var categories : ArrayList<String>,
                    var ingredients : ArrayList<String>,
                    var title: String,
                    var components : ArrayList<String>,
                    var calories: String,
                    var carbohydrate : String,
                    var protein : String,
                    var sodium : String,
                    var fat : String,
                    var id : String)


data class SimpleFoodData(var id: String,
                          var url: String,
                          var title: String,
                          var percent: Float,
                          var starRate: String) : Serializable




//모델과 일치율에 이용되는 데이터셋
data class FoodPointData(var id: String,
                         var point : Float)
data class FoodComponentsData(var id : String,
                              var components : ArrayList<String>)
data class InitFoodGroceryData(var id : String,
                              var groceryName : String){
    companion object {
        val TABLE_NAME = "init_food_grocery_data_table"
        val COLUMN_FOOD_ID = "food_id"
        val COLUMN_GROCERY_NAME = "food_name"
    }
}
data class FoodPersentData(var id: String,
                           var persent: Float)