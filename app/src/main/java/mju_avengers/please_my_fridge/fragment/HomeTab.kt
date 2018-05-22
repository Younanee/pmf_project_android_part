package mju_avengers.please_my_fridge.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.MyConnectFirebase
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodInfoRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.uiThread

class HomeTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val foodName : String = foodItems[idx].title
        startActivity<DetailedFoodInfoActivity>("title" to foodName)
    }
    lateinit var foodItems : ArrayList<FoodData>
    lateinit var foodInfoDataAdapter : FoodInfoRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHomeFoodAdapter()
//        doAsync {
//            foodItems = setTestFoodData()
//            uiThread {
//                foodInfoDataAdapter.notifyDataSetChanged()
//            }
//        }
    }

    fun setTestFoodData() : ArrayList<FoodData>{
//        mainFoodItems.add(FoodData("null", arrayListOf("1"), "삼겹살 덮밥",95, 4.5.toFloat(), ArrayList(), ArrayList()))
//        mainFoodItems.add(FoodData("null", arrayListOf("1"), "차돌박이 찌개",82, 4.toFloat(), ArrayList(), ArrayList()))
//        mainFoodItems.add(FoodData("null", arrayListOf("1"), "양배추피 만두",79, 3.7.toFloat(), ArrayList(), ArrayList()))
        return MyConnectFirebase.getInstence(activity!!).getFoodDatas(arrayListOf("10","20"))
    }

    fun setHomeFoodAdapter(){
        foodItems = ArrayList()
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, foodItems)
        foodInfoDataAdapter.setOnItemClickListener(this)
        hoomAnimationAdapter = AlphaInAnimationAdapter(foodInfoDataAdapter)
        home_food_rv.layoutManager = LinearLayoutManager(context)
        home_food_rv.adapter = hoomAnimationAdapter
    }

}