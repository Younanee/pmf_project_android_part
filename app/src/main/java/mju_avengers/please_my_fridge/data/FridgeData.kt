package mju_avengers.please_my_fridge.data

data class FridgeData(val _id : String, val ingredients : ArrayList<GroceryData>)
data class GroceryData(val name: String, val category : GroceryCategory)
enum class GroceryCategory{
    MEAT, SEAFOOD, VEGETABLE, FRUIT, SAUCE, GRAIN, ETC
}