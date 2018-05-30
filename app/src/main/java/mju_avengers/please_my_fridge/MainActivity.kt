package mju_avengers.please_my_fridge

import android.app.ProgressDialog
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter
import mju_avengers.please_my_fridge.data.*
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.match_persent.MakeMatchRate
import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.longToast
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    private val FINISH_INTERVAL_TIME : Long = 2000
    private var backPressedTime : Long = 0
//    lateinit var mFoodDatabase : DatabaseReference
    lateinit var simpleFoodItems : ArrayList<SimpleFoodData>
    var mProgressDialog : ProgressDialog? = null
    private var dataCount = 0
    private var dataSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleFoodItems = ArrayList()
        configureTabLayout()
        if (mProgressDialog == null) {
            mProgressDialog = indeterminateProgressDialog("데이터 불러오기...")
            mProgressDialog!!.show()
        } else {
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }
        }
        setSimpleFoodItems()

    }


    private fun configureTabLayout() {
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_home_unclicked_black_24dp), 0)
        main_tabLayout.getTabAt(0)!!.icon!!.alpha = 255
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_search_black_24dp), 1)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_kitchen_black_24dp), 2)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_person_black_24dp), 3)

        val tabAdapter = TabPagerAdapter(main_tabLayout.tabCount, supportFragmentManager)
        main_pager.adapter = tabAdapter
        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tabLayout))
        main_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 63
                main_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 255
                main_pager.currentItem = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 63
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
    private fun setSimpleFoodItems(){
        var matchPersnetData : ArrayList<FoodPersentData> = getComponentsMatchPercentDatas()
        dataSize = matchPersnetData.size
        matchPersnetData.forEach {
            getSimpleFoodData(it)
        }

    }

    private fun getComponentsMatchPercentDatas() : ArrayList<FoodPersentData>{
        //중복 식료품 없앤 쿼리로 넣기
        var foodComponentsDatas : ArrayList<FoodComponentsData> = getComponentsData()
        var myFridgeGroceriesData : ArrayList<String> = DataOpenHelper.getInstance(this!!).getAllGrocerisNameInFridge()
        var matchPersnetData : ArrayList<FoodPersentData> = MakeMatchRate(myFridgeGroceriesData).getDirectory(foodComponentsDatas)
//        var cursor: Cursor = DataOpenHelper.getInstance(this).readableDatabase.query(true,
//                GroceryData.TABLE_NAME,
//                arrayOf(GroceryData.COLUMN_NAME),
//                null,null,null,null,null,null)
        return matchPersnetData
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
                var starRate = 4.5.toFloat()
                simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == simpleFoodItems.size && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    dataCount = 0
                    dataSize = 0
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }


    private fun getComponentsData(): ArrayList<FoodComponentsData>{
        val recipeid : IntArray = IntArray(5023, {i -> i})
        var foodComponentsDatas: ArrayList<FoodComponentsData> = ArrayList()
        var reipePointDatas : ArrayList<FoodPointData> = loadModel(recipeid)
        reipePointDatas.forEach {
            foodComponentsDatas.add(getFoodComponentsData(it.id))
        }
        return foodComponentsDatas
    }
    private fun loadModel(recipeid : IntArray) : ArrayList<FoodPointData>{
        var mRecommeders = TensorflowRecommend.create(applicationContext!!.assets, "Keras",
                "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
                "merge_1/ExpandDims")
        var foodPoints : ArrayList<FoodPointData> = ArrayList()
        val id = 0
        //val recipeid : IntArray = IntArray(5023, {i -> i})
        for (i in recipeid.indices) {
            val rec = mRecommeders.recognize(id, recipeid[i])
            foodPoints.add(FoodPointData(rec.label, rec.conf))
        }
        foodPoints.sortByDescending { foodPointData -> foodPointData.point }

        return foodPoints.take(20) as ArrayList<FoodPointData>
    }
    private fun getFoodComponentsData(foodId : String) : FoodComponentsData {
        var result : FoodComponentsData? = DataOpenHelper.getInstance(this!!).mappingDBDataToFoodComponentsData(foodId)

        return result!!
    }
}
