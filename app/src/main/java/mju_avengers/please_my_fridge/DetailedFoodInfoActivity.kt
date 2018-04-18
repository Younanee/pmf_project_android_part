package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detailed_food_info.*

class DetailedFoodInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food_info)

        val name = intent.getStringExtra("name")
        detail_food_name_tv.text = name
    }
}
