package mju_avengers.please_my_fridge.data

data class FridgeData(val _id : String, val ingredients : ArrayList<IngredientData>)
data class IngredientData(val name: String, val category : Ingredient_category)
enum class Ingredient_category{
    MEAT, VEGETABLE, SEAFOOD, FRUIT, SAUCE, GRAIN
}