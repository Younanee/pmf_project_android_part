package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detailed_food_info.*
import mju_avengers.please_my_fridge.data.FoodData

class DetailedFoodInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food_info)
        val intentData = intent


        BottomSheetDialog(this).show()

    }
}
