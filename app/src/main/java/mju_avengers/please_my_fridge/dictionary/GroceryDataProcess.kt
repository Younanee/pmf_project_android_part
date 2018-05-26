package mju_avengers.please_my_fridge.dictionary

class GroceryDataProcess() {
    companion object {
        private var instance : GroceryDataProcess? = null
        fun getInstence() : GroceryDataProcess{
            if (instance == null) {
                instance = GroceryDataProcess()
            }
            return instance!!
        }
    }
    fun returnData(cameraOutputText : String) : List<String>{
        var dataList : List<String> = Processing(divideString(cameraOutputText)).gredients
        return dataList
    }
    fun divideString(tagetString : String) : Array<String>{
        return tagetString.split("\n").toTypedArray()
    }

}

