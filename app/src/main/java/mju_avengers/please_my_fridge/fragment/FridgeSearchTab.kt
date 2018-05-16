package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fridge_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.TestFoodData
import org.jetbrains.anko.support.v4.startActivity


class FridgeSearchTab : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx : Int = fridge_search_rv.getChildAdapterPosition(v)
        val foodName : String = testFoodDatas[idx].name
        startActivity<DetailedFoodInfoActivity>("title" to foodName)
    }

    lateinit var testFoodDatas: ArrayList<TestFoodData>
    lateinit var foodDataAdapter: FoodRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_fridge_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fridge_search_btn.setOnClickListener {
            testDataSet()
            fridge_search_btn.visibility = View.GONE
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
        fridge_search_rv.layoutManager = LinearLayoutManager(context)
        fridge_search_rv.adapter = foodDataAdapter
    }
}