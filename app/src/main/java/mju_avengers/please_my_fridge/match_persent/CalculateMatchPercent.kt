package mju_avengers.please_my_fridge.match_persent

import android.content.Context
import android.util.Log
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.db.DataOpenHelper

class CalculateMatchPercent(val ctx : Context) {
    fun matchPercentCalculator(data : ArrayList<FoodComponentsData>) : ArrayList<FoodPersentData>{

        var foodComponentsDatas : ArrayList<FoodComponentsData> = data

        var myFridgeComponentsData : ArrayList<String> = DataOpenHelper.getInstance(ctx).getAllGrocerisNameInFridge()
        var foodPercentDatas : ArrayList<FoodPersentData> = ArrayList()

        foodComponentsDatas.forEach {
            val foodId = it.id
            val components : ArrayList<String> = it.components
            val componentSize = components.size

            var havingCount = 0
            var matchPercent : Float
            components.forEach {
                if(myFridgeComponentsData.contains(it)){
                    havingCount++
                }
            }

            matchPercent = (havingCount.toFloat()/componentSize.toFloat())*100

            foodPercentDatas.add(FoodPersentData(foodId, matchPercent))
        }

        return foodPercentDatas
    }
}