package mju_avengers.please_my_fridge.data

data class GroceryData(val id: Int, val category : String, val name: String){
    companion object {
        val COLUMN_ID = "id"
        val TABLE_NAME = "grocery_table"
        val COLUMN_NAME = "name"
        val COLUMN_CATEGORY = "category"
    }
}
data class EatenFoodData(val id: Int, val foodId : String){
    companion object {
        val COLUMN_ID = "id"
        val TABLE_NAME = "eaten_foods_table"
        val COLUMN_FOOD_ID = "food_id"
    }
}


enum class GroceryCategory{
    MEAT, SEAFOOD, VEGETABLE, FRUIT, SAUCE, GRAIN, ETC
}