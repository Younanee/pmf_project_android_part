package mju_avengers.please_my_fridge

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_main.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.data.FoodPointData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.match_persent.CalculateMatchPercent
import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity() {

    private var dataCount = 0
    private var dataSize = 0
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0
    lateinit var simpleFoodItems: ArrayList<SimpleFoodData>
    lateinit var mProgressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgressDialog = indeterminateProgressDialog("초기 데이터 로딩")
        simpleFoodItems = ArrayList()

        startSettingFragmentView()
    }

    private fun configureTabAndFragmentView(datas: ArrayList<SimpleFoodData>) {
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_home_unclicked_black_24dp), 0)
        //main_tabLayout.getTabAt(0)!!.icon!!.alpha = 255
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
        var tempTime: Long = System.currentTimeMillis()
        var intervalTime: Long = tempTime - backPressedTime
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            longToast("한번 더 뒤로가기를 누르면 종료됩니다.")
        }
    }

    private fun startSettingFragmentView() {


        var percentData: ArrayList<FoodPersentData> = getComponentsMatchPercentData()
        dataSize = percentData.size
        percentData.forEach {
            getSimpleFoodData(it)
        }
    }

    private fun getComponentsMatchPercentData(): ArrayList<FoodPersentData> {

        var foodComponentsDatas: ArrayList<FoodComponentsData> = getComponentsData()

        return CalculateMatchPercent(this).matchPercentCalculator(foodComponentsDatas)
    }

    private fun getSimpleFoodData(childData: FoodPersentData) {
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener {
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


    private fun getComponentsData(): ArrayList<FoodComponentsData> {
        var pointData: ArrayList<FoodPointData> = loadModel()
        var childIds: ArrayList<String> = ArrayList()
        pointData.forEach {
            childIds.add(it.id)
        }
        return getFoodComponentsDataInDB(childIds)
    }

    private fun getFoodComponentsDataInDB(foodIds: ArrayList<String>): ArrayList<FoodComponentsData> {
        var result: ArrayList<FoodComponentsData>? = DataOpenHelper.getInstance(this!!).mappingDBDataToFoodComponentsData(foodIds)
        return result!!
    }

    private fun loadModel(): ArrayList<FoodPointData> {
        var mRecommeders = TensorflowRecommend.create(applicationContext!!.assets, "Keras",
                "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
                "merge_1/ExpandDims")

        val allRecipeID = IntArray(5023, { i -> i })
        var ids = allRecipeID.toList() as ArrayList<Int>
        DataOpenHelper.getInstance(this!!).getEatenFoodDatas().forEach {
            ids.remove(it.toInt())
        }

        val id = 0
        var foodPoints: ArrayList<FoodPointData> = ArrayList()
        ids.forEach {
            val rec = mRecommeders.recognize(id, it)
            foodPoints.add(FoodPointData(rec.label, rec.conf))
        }

        foodPoints.sortByDescending { foodPointData -> foodPointData.point }

        return foodPoints.take(20) as ArrayList<FoodPointData>
    }



    //새 데이터를 얻기 위해 HomeTab과 SearchTab에서 사용
    fun getNewMatchPercentData(): ArrayList<FoodPersentData> {
        return getComponentsMatchPercentData()
    }
}
