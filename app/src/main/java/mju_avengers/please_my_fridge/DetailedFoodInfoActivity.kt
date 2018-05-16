package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detailed_food_info.*
import mju_avengers.please_my_fridge.data.FoodData

class DetailedFoodInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food_info)
        val intentData = intent

        //detail_food_title_tv.text = intentData.getStringExtra("title")
        //detail_food_direction_tv.text = intentData.getStringArrayListExtra("ingredients")[0] + "\n " + intent.getStringArrayListExtra("ingredients")[1]
    }
}
