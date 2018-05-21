package mju_avengers.please_my_fridge.data

data class FridgeData(val _id : String, val ingredients : ArrayList<GroceryData>)
data class GroceryData(val id: Int, val category : String, val name: String){
    companion object {
        val COLUMN_ID = "id"
        val TABLE_NAME = "grocery_table"
        val COLUMN_NAME = "name"
        val COLUMN_CATEGORY = "category"
    }
}


enum class GroceryCategory{
    MEAT, SEAFOOD, VEGETABLE, FRUIT, SAUCE, GRAIN, ETC
}