package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.TestFoodData
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class SearchTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx : Int = search_food_rv.getChildAdapterPosition(v)
        val foodName : String = testFoodDatas[idx].name
        startActivity<DetailedFoodInfoActivity>("foodName" to foodName)
    }
    lateinit var testFoodDatas: ArrayList<TestFoodData>
    lateinit var foodDataAdapter: FoodRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_bottom_navi.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_grocery -> {
                    toast("식재료 기반")
                    true
                }
                R.id.action_fridge -> {
                    toast("냉장고 기반")
                    true
                }
                R.id.action_info -> {
                    alert {
                        title = "검색 방법 소개"
                        message = "식재료 기반 : \n 냉장고 기반 :"
                        negativeButton("닫기", {

                        })
                    }.show()
                    true
                }

                else -> {
                    false
                }
            }
        }

    }
    fun testDataSet(){
        testFoodDatas = ArrayList()
        testFoodDatas.add(TestFoodData(R.drawable.t01, "닭강정", 97))
        testFoodDatas.add(TestFoodData(R.drawable.t02, "닭꼬치", 91))
        testFoodDatas.add(TestFoodData(R.drawable.t03, "훈제닭", 86))
        testFoodDatas.add(TestFoodData(R.drawable.t04, "짜장면", 85))
        testFoodDatas.add(TestFoodData(R.drawable.t07, "닭꼬치 볶음밥", 59))
        testFoodDatas.add(TestFoodData(R.drawable.t09, "닭카레", 51))
        testFoodDatas.add(TestFoodData(R.drawable.t05, "삼겹살", 74))
        testFoodDatas.add(TestFoodData(R.drawable.t06, "쭈삼", 68))

        foodDataAdapter = FoodRecyclerAdapter(context!!, testFoodDatas)
        foodDataAdapter.setOnItemClickListener(this)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.adapter = foodDataAdapter
    }



}