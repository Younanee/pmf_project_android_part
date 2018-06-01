package mju_avengers.please_my_fridge

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.load.engine.Initializable
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter
import mju_avengers.please_my_fridge.data.*
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.match_persent.CalculateMatchPercent
import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity() {
    private val FINISH_INTERVAL_TIME : Long = 2000
    private var backPressedTime : Long = 0
    lateinit var simpleFoodItems : ArrayList<SimpleFoodData>
    var mProgressDialog : ProgressDialog? = null
    private var dataCount = 0
    private var dataSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleFoodItems = ArrayList()

        if (mProgressDialog == null) {
            mProgressDialog = indeterminateProgressDialog("Data Loading...")
            mProgressDialog!!.show()
        } else {
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }
        }
        startSettingFragmentView()

    }
    private fun configureTabAndFragmentView(datas : ArrayList<SimpleFoodData>) {
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_home_unclicked_black_24dp), 0)
        main_tabLayout.getTabAt(0)!!.icon!!.alpha = 255
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_search_black_24dp), 1)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_kitchen_black_24dp), 2)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_person_black_24dp), 3)

        val tabAdapter = TabPagerAdapter(main_tabLayout.tabCount, supportFragmentManager, datas)
        main_pager.adapter = tabAdapter
        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tabLayout))
        main_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 255
                main_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 255
                main_pager.currentItem = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 255
                main_pager.currentItem = tab!!.position
            }
        })
    }
    override fun onBackPressed() {
        var tempTime : Long = System.currentTimeMillis()
        var intervalTime : Long = tempTime - backPressedTime
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            longToast("한번 더 뒤로가기를 누르면 종료됩니다.")
        }
    }
    private fun startSettingFragmentView(){
        var matchPersnetData : ArrayList<FoodPersentData> = getComponentsMatchPercentDatas()
//        matchPersnetData.sortByDescending { foodPersentData -> foodPersentData.persent }
        dataSize = matchPersnetData.size
        matchPersnetData.forEach {
            getSimpleFoodData(it)
        }
    }

    fun getNewMatchPercentData() : ArrayList<FoodPersentData> {
        return getComponentsMatchPercentDatas()
    }


    private fun getSimpleFoodData(childData : FoodPersentData){
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener{
            override fun onStart() {
            }
            override fun onSuccess(data: DataSnapshot) {
                dataCount++

                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == simpleFoodItems.size && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    dataCount = 0
                    dataSize = 0

                    configureTabAndFragmentView(simpleFoodItems)
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }
    private fun getComponentsMatchPercentDatas() : ArrayList<FoodPersentData>{

        var foodComponentsDatas : ArrayList<FoodComponentsData> = getComponentsData()

        return CalculateMatchPercent(this).matchPercentCalculator(foodComponentsDatas)
    }

    private fun getComponentsData(): ArrayList<FoodComponentsData>{
        var recipeid : IntArray = IntArray(5023, {i -> i})
        var foodIds : ArrayList<Int> = recipeid.toList() as ArrayList<Int>
        //먹은 음식 불러오기
        DataOpenHelper.getInstance(this!!).getEatenFoodDatas().forEach {
            foodIds.remove(it.toInt())
        }

        //var recipeIds : ArrayList<Int> = ArrayList()
//        for (i in 0..5022)
        //디비에서 끌어온 이미 먹은 음식들 빼주기@!@#!@#!@#!@#!@#!@#!@#
        //recipeid.
        var reipePointDatas : ArrayList<FoodPointData> = loadModel(foodIds)
        var childIds : ArrayList<String> = ArrayList()
        reipePointDatas.forEach {
            childIds.add(it.id)
        }
        return getFoodComponentsData(childIds)
    }
    private fun getFoodComponentsData(foodIds : ArrayList<String>) : ArrayList<FoodComponentsData> {
        var result : ArrayList<FoodComponentsData>? = DataOpenHelper.getInstance(this!!).mappingDBDataToFoodComponentsData(foodIds)
        Log.e("디비에서 넘어온 compoenents들 ", result!!.size.toString())
        return result!!
    }

    private fun loadModel(foodIds: ArrayList<Int>): ArrayList<FoodPointData> {
        var mRecommeders = TensorflowRecommend.create(applicationContext!!.assets, "Keras",
                "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
                "merge_1/ExpandDims")
        var foodPoints: ArrayList<FoodPointData> = ArrayList()
        val id = 0

        foodIds.forEach {
            val rec = mRecommeders.recognize(id, it)
            foodPoints.add(FoodPointData(rec.label, rec.conf))
        }

        foodPoints.sortByDescending { foodPointData -> foodPointData.point }

        return foodPoints.take(20) as ArrayList<FoodPointData>
    }


}
