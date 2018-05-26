package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_detailed_food_info.*
import mju_avengers.please_my_fridge.adapter.RecipeRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import org.jetbrains.anko.indeterminateProgressDialog

class DetailedFoodInfoActivity : AppCompatActivity() {
    lateinit var recipeRecyclerAdapter: RecipeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food_info)
        detail_food_close_btn.setOnClickListener {
            finish()
        }
        val childId = intent.getStringExtra("childId")
        readDetailedFoodData(childId)
    }

    fun setView(foodData: FoodData) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_image_black_24dp)
        requestOptions.error(R.drawable.ic_clear_black_36dp)
        requestOptions.centerCrop()
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(foodData.urls[0])
                .into(detail_food_img_iv)
        detail_food_title_tv.text = foodData.title
        detail_food_calories_tv.text = foodData.calories
        detail_food_carbohydrate_tv.text = foodData.carbohydrate
        detail_food_protein_tv.text = foodData.protein
        detail_food_sodium_tv.text = foodData.sodium
        detail_food_fat_tv.text = foodData.fat

        var directions : ArrayList<String> = foodData.directions
        var urls : ArrayList<String> = foodData.urls

        recipeRecyclerAdapter = RecipeRecyclerAdapter(this, directions, urls)
        detail_food_recipe_rv.layoutManager = LinearLayoutManager(this)
        detail_food_recipe_rv.adapter = recipeRecyclerAdapter
    }

    private fun readDetailedFoodData(childId: String) {
        var mProgressDialog = indeterminateProgressDialog("Loading...")
        UseFirebaseDatabase.getInstence().readFBData(childId, object : OnGetDataListener {
            override fun onStart() {
                mProgressDialog.show()
            }

            override fun onSuccess(data: DataSnapshot) {
                val urls: ArrayList<String> = ArrayList()
                data!!.child("url").children.forEach {
                    urls.add(it.value.toString())
                }
                val directions: ArrayList<String> = ArrayList()
                data!!.child("directions").children.forEach {
                    directions.add(it.value.toString())
                }
                val categories: ArrayList<String> = ArrayList()
                data!!.child("categories").children.forEach {
                    categories.add(it.value.toString())
                }
                val ingredients: ArrayList<String> = ArrayList()
                data!!.child("ingredients").children.forEach {
                    ingredients.add(it.value.toString())
                }
                val title = data!!.child("title").value.toString()
                val components: ArrayList<String> = ArrayList()
                data!!.child("components").children.forEach {
                    components.add(it.value.toString())
                }
                val calories = data!!.child("calories").value.toString()
                val carbohydrate = data!!.child("carbohydrate").value.toString()
                val protein = data!!.child("protein").value.toString()
                val sodium = data!!.child("sodium").value.toString()
                val fat = data!!.child("fat").value.toString()
                var id = data!!.child("id").value.toString()
                val foodData = FoodData(urls, directions, categories, ingredients, title, components, calories, carbohydrate, protein, sodium, fat, id)
                setView(foodData)
                if (mProgressDialog.isShowing) {
                    mProgressDialog.dismiss()
                }
            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.e("fail", databaseError.toString())
            }
        })
    }
}
