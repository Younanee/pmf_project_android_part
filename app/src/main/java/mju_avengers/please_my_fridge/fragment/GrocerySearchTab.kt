package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_grocery_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.TestFoodData
import org.jetbrains.anko.support.v4.startActivity

class GrocerySearchTab : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx : Int = grocery_search_rv.getChildAdapterPosition(v)
        val foodName : String = testFoodDatas[idx].name
        startActivity<DetailedFoodInfoActivity>("foodName" to foodName)
    }
    lateinit var testFoodDatas: ArrayList<TestFoodData>
    lateinit var foodDataAdapter: FoodRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grocery_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        grocery_search_input_layout.hint = "식재료를 입력해주세요."

        grocery_search_btn.setOnClickListener {
            var keyword : String = grocery_search_input_et.text.toString()
            grocery_search_keyword_tv.text = "\""+keyword+"\""
            testDataSet()
        }

    }
    fun testDataSet(){
        testFoodDatas = ArrayList()
        testFoodDatas.add(TestFoodData(R.drawable.t01, "닭강정", 97))
        testFoodDatas.add(TestFoodData(R.drawable.t02, "닭꼬치", 91))
        testFoodDatas.add(TestFoodData(R.drawable.t03, "훈제닭", 86))
        testFoodDatas.add(TestFoodData(R.drawable.t07, "닭꼬치 볶음밥", 59))
        testFoodDatas.add(TestFoodData(R.drawable.t09, "닭카레", 51))

        foodDataAdapter = FoodRecyclerAdapter(context!!, testFoodDatas)
        foodDataAdapter.setOnItemClickListener(this)
        grocery_search_rv.layoutManager = LinearLayoutManager(context)
        grocery_search_rv.adapter = foodDataAdapter
    }
}