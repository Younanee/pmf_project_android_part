package mju_avengers.please_my_fridge

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_detailed_food.*
import kotlinx.android.synthetic.main.activity_detailed_food_info.*
import mju_avengers.please_my_fridge.adapter.DetailedFoodTabPagerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.toast

class DetailedFoodActivity : AppCompatActivity() {
    lateinit var childId : String
    lateinit var foodData : FoodData
    lateinit var matchPercent : String
    var isEaten : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food)
        if(intent.hasExtra("childId")){
            childId = intent.getStringExtra("childId")
            matchPercent = intent.getStringExtra("matchPercent")

            readDetailedFoodData(childId)

            isEaten = DataOpenHelper.getInstance(this!!).getEatenFoodDatas().contains(childId)
        }


        if (isEaten){
            detailed_food_appbar_add_eaten_btn.setImageResource(R.drawable.ic_star_yellow_24dp)
        } else {
            detailed_food_appbar_add_eaten_btn.setImageResource(R.drawable.ic_star_border_black_24dp)
        }
        detailed_food_appbar_add_eaten_btn.setOnClickListener {
            checkEatenBtn()
        }




        detailed_food_appbar_back_btn.setOnClickListener {
            finish()
        }
        //configureTabLayout()
        //readDetailedFoodData(childId)
    }
    fun checkEatenBtn(){
        if(isEaten){
            detailed_food_appbar_add_eaten_btn.setImageResource(R.drawable.ic_star_border_black_24dp)
            isEaten = false
            DataOpenHelper.getInstance(this).removeEatenFoodData(childId)
        } else {
            detailed_food_appbar_add_eaten_btn.setImageResource(R.drawable.ic_star_yellow_24dp)
            isEaten = true
            DataOpenHelper.getInstance(this).insertEatenFoodDatas(childId)
        }
    }

    private fun configureTabLayout() {
        detailed_food_tabLayout.addTab(detailed_food_tabLayout.newTab().setText("음식 정보"), 0)
        detailed_food_tabLayout.addTab(detailed_food_tabLayout.newTab().setText("재료"), 1)
        detailed_food_tabLayout.addTab(detailed_food_tabLayout.newTab().setText("레시피"), 2)

        val tabAdapter = DetailedFoodTabPagerAdapter(detailed_food_tabLayout.tabCount, supportFragmentManager)
        detailed_food_pager.adapter = tabAdapter
        detailed_food_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(detailed_food_tabLayout))
        detailed_food_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                detailed_food_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                detailed_food_pager.currentItem = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                detailed_food_pager.currentItem = tab!!.position
            }
        })
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
                foodData = FoodData(urls, directions, categories, ingredients, title, components, calories, carbohydrate, protein, sodium, fat, id)
                configureTabLayout()
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