package mju_avengers.please_my_fridge.match_persent

import android.content.Context
import android.util.Log
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.db.DataOpenHelper

class CalculateMatchPercent(val ctx : Context) {
    fun matchPercentCalculator(data : ArrayList<FoodComponentsData>) : ArrayList<FoodPersentData>{

        var foodComponentsDatas : ArrayList<FoodComponentsData> = data
//        data.forEach {
//            Log.e("[디비 속 FoodComponentsData] " , "${it.id}")
//            Log.e("[디비 속 FoodComponentsData] " , "${it.components}")
//        }
//        Log.e("계산해야될 받아온 데이터 사이즈 : " , foodComponentsDatas.size.toString())
        var myFridgeComponentsData : ArrayList<String> = DataOpenHelper.getInstance(ctx).getAllGrocerisNameInFridge()
//        Log.e("나의 냉장고 속 데이터들 : " , myFridgeComponentsData.toString())
        var foodPercentDatas : ArrayList<FoodPersentData> = ArrayList()

        foodComponentsDatas.forEach {
            val foodId = it.id
            val components : ArrayList<String> = it.components
            val componentSize = components.size

            var havingCount = 0
            var matchPercent : Float
            Log.e("계산중...", "재료 : ${components.toString()}")
            components.forEach {
                if(myFridgeComponentsData.contains(it)){
                    Log.e("계산중...", "있는 재료 : $it /")
                    havingCount++
                }
            }

            matchPercent = (havingCount.toFloat()/componentSize.toFloat())*100

            val percent : String = String.format("%.2f", matchPercent)

            Log.e("계산중...", "아이디: $foodId, 재료수: $componentSize, 일치수: $havingCount , 일치율: $percent")

            foodPercentDatas.add(FoodPersentData(foodId, percent))
        }

        return foodPercentDatas
    }
}