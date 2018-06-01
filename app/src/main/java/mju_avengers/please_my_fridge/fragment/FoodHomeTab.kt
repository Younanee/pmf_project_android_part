package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_detailed_food_info.*
import kotlinx.android.synthetic.main.fragment_food_home_tab.*
import mju_avengers.please_my_fridge.DetailedFoodActivity
import mju_avengers.please_my_fridge.OnGetDataListener
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.UseFirebaseDatabase
import mju_avengers.please_my_fridge.adapter.CategoryRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast


class FoodHomeTab : Fragment() {
    lateinit var categoryRecyclerAdapter : CategoryRecyclerAdapter
    lateinit var categories : ArrayList<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food_home_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCategoryRecyclerAdapter()
        setView((activity as DetailedFoodActivity).foodData)
    }

    fun setView(foodData: FoodData) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_image_black_24dp)
        requestOptions.error(R.drawable.ic_clear_black_36dp)
        requestOptions.centerCrop()
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(foodData.urls[0])
                .into(detail_food_home_img_iv)
        detail_food_home_title_tv.text = foodData.title
        detail_food_home_calories_tv.text = foodData.calories
        detail_food_home_carbohydrate_tv.text = foodData.carbohydrate
        detail_food_home_protein_tv.text = foodData.protein
        detail_food_home_sodium_tv.text = foodData.sodium
        detail_food_home_fat_tv.text = foodData.fat
        detail_food_home_persent_tv.text = (activity as DetailedFoodActivity).matchPercent + "%"
    }
    private fun setCategoryRecyclerAdapter() {
        categories = ArrayList((activity as DetailedFoodActivity).foodData.categories)
        categoryRecyclerAdapter = CategoryRecyclerAdapter(context!!, categories)
        detail_food_home_grocery_category_rv.layoutManager = LinearLayoutManager(context, 0, false)
        detail_food_home_grocery_category_rv.adapter = categoryRecyclerAdapter
    }
}
